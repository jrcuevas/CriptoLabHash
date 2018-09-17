/*
 * Copyright (C) 2018 José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package criptolabhash.ataques.servidor;

import criptolabhash.ataques.Bytes;
import criptolabhash.ataques.Colisiones;
import criptolabhash.control.APIServerHash;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Mantiene un control centralizado de los códigos hash generados en los
 * clientes.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class DataBaseHashServer implements APIServerHash {

    /**
     * Tamaño máximo de segmentos en el mapa hash.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Algoritmo hash a utilizar por los clientes.
     */
    private String algoritmo;

    /**
     * Tamaño en bytes de los mensajes generados por los clientes.
     */
    private int sizemsg;

    /**
     * Tamaño en bytes del hash calculado en los clientes.
     */
    private int sizehash;

    /**
     * Almacena los clientes que se conectan.
     */
    private final List<HiloServidorCliente> clientes;

    /**
     * Indica si los clientes deben estar calculando hash.
     */
    private boolean clientesactivos;

    /**
     * Almacena los hash que van generando los clientes, junto con el nombre del
     * cliente que lo ha enviado.
     */
    private Map<Bytes, HiloServidorCliente> dbhash;

    /**
     * Número de hashses almacenados.
     */
    private volatile Long nhashes;

    /**
     * Almacena los hashes asociados con una lista de mensajes que los producen.
     */
    private Colisiones colisiones;
    
    /**
     * Información sobre ip y puerto de escucha.
     */
    private String infoConexion;

    /**
     * Constructor que inicializa las bases de datos.
     *
     * @param algoritmo Algoritmo inicial a utilizar.
     * @param sizemsg Tamaño del mensaje.
     * @param infoConexion Información de ip y puerto de escucha del servidor.
     */
    public DataBaseHashServer(String algoritmo, int sizemsg, String infoConexion) {
        this.algoritmo = algoritmo;
        this.sizemsg = sizemsg;
        this.sizehash = 0;
        this.clientes = Collections.synchronizedList(new ArrayList());
        this.infoConexion = infoConexion;
        resetDB();
    }

    /**
     * Elimina la base de datos y setea las banderas.
     */
    private void resetDB() {
        this.clientesactivos = false;
        //this.dbhash = Collections.synchronizedMap(new HashMap(MAXIMUM_CAPACITY / 2));
        this.dbhash = Collections.synchronizedMap(new HashMap());
        this.colisiones = new Colisiones();
        this.nhashes = 0L;
    }

    /**
     * Almacena el hash proporcionado por el cliente. Si ya hay otro igual, pide
     * a los clientes el mensaje que generó los hash. Si los mensajes son
     * iguales incrementa las pseudocolisiones. Si los mensajes son distintos
     * añade los mensajes a la lista de colisiones.
     *
     * @param hash Código has generado.
     * @param cliente Cliente que lo ha generado.
     */
    protected synchronized void addHash(Bytes hash, HiloServidorCliente cliente) {
        if (hash == null || cliente == null){
            return;
        }
        HiloServidorCliente cliente2 = dbhash.put(hash, cliente);
        if (cliente2 != null) {
            Bytes ultimomensaje = cliente.getMessageCliente(hash, true);
            Bytes mensajeanterior = cliente2.getMessageCliente(hash, false);
            colisiones.addColision(hash, ultimomensaje, mensajeanterior);
        }
        nhashes++;
    }

    /**
     * Recibe el registro del cliente.
     *
     * @param cliente Cliente que inicia el registro.
     */
    protected synchronized void registroCliente(HiloServidorCliente cliente) {
        this.clientes.add(cliente);
        cliente.setFuncionCliente(algoritmo, sizemsg, sizehash);
    }

    /**
     * Elimina el cliente de la lista de clientes.
     *
     * @param cliente Canal de conexión con el cliente.
     */
    protected synchronized void eliminaCliente(HiloServidorCliente cliente) {
        clientes.remove(cliente);
    }

    /**
     * Indica si los clientes deben estar activos.
     *
     * @return true si clientes deben estar calculando.
     */
    protected boolean isClientesactivos() {
        return this.clientesactivos;
    }

    @Override
    public void resetearSistema() {
        Iterator<HiloServidorCliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            HiloServidorCliente cliente = iterator.next();
            cliente.stopCliente();
            cliente.resetCliente();
        }
        resetDB();
    }

    @Override
    public synchronized void setAlgoritmo(String algoritmo, int sizemsg, int sizehash) {
        this.algoritmo = algoritmo;
        this.sizemsg = sizemsg;
        this.sizehash = sizehash;
        Iterator<HiloServidorCliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            HiloServidorCliente cliente = iterator.next();
            cliente.setFuncionCliente(this.algoritmo, this.sizemsg, this.sizehash);
        }
    }

    @Override
    public synchronized void arrancaAtaque() {
        clientesactivos = true;
        Iterator<HiloServidorCliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            HiloServidorCliente cliente = iterator.next();
            cliente.startCliente();
        }
    }

    @Override
    public synchronized void paraAtaque() {
        clientesactivos = false;
        Iterator<HiloServidorCliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            HiloServidorCliente cliente = iterator.next();
            cliente.stopCliente();
        }
    }

    @Override
    public synchronized void desconectaClientes() {
        Iterator<HiloServidorCliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            HiloServidorCliente cliente = iterator.next();
            cliente.apagaCliente();
        }
        this.resetDB();
    }

    @Override
    public int nClientesConectados() {
        return this.clientes.size();
    }

    @Override
    public long nHashCalculados() {
        return this.nhashes;
    }

    @Override
    public int nColisiones() {
        return this.colisiones.getNgrupos();
    }

    @Override
    public int nPseudoColisiones() {
        return this.colisiones.getPseudocolisiones();
    }

    @Override
    public String getHashName() {
        return this.algoritmo;
    }

    @Override
    public int getSizeMsg() {
        return this.sizemsg;
    }

    @Override
    public int getSizeHash() {
        return this.sizehash;
    }

    @Override
    public String getInformeColisiones() {
        String informe = this.colisiones.getInformeColisiones();
        if (informe == null || informe.equals("")){
            informe = "Hasta ahora no se han encontrado colisiones.";
        }
        return informe;
    }

    @Override
    public String getInfoConexion() {
        return this.infoConexion;
    }
}

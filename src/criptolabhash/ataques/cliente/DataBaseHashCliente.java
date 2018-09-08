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
package criptolabhash.ataques.cliente;

import criptolabhash.ataques.Bytes;
import criptolabhash.ataques.Colisiones;
import criptolabhash.ataques.servidor.FuncionesCliente;
import criptolabhash.ataques.NoSetException;
import criptolabhash.control.APIClienteHash;
import criptolabhash.funciones.FabricaHash;
import criptolabhash.funciones.Hash32;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class DataBaseHashCliente extends Thread implements FuncionesCliente, APIClienteHash {

    /**
     * Tamaño máximo de segmentos en el mapa hash.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Algoritmo hash a utilizar por el cliente.
     */
    private Hash32 funcionhash;

    /**
     * Generador de mensajes aleatorios.
     */
    private GeneradorMensaje generador;

    /**
     * Tamaño en bytes del hash generado que se tendrán en cuenta.
     */
    private int sizehash;

    /**
     * Mensajes asociados a los hash generados por el cliente.
     */
    private Map<Bytes, Bytes> mensajes;

    /**
     * Almacena los hashes asociados con una lista de mensajes que los producen.
     */
    private Colisiones colisiones;

    /**
     * Número de hashses generados.
     */
    private volatile Long nhashes;

    /**
     * Conexión con el servidor.
     */
    private final HiloClienteServidor conexionservidor;

    /**
     * Indica si el generador esta activo o no.
     */
    private volatile boolean activo;

    /**
     * Indica que se ha de realizar un reset.
     */
    private volatile boolean reset;

    /**
     * Indica si se debe acabar la ejecución del cliente.
     */
    private volatile boolean shutdown;

    /**
     * Construcción de la base de datos y conexión al servidor.
     *
     * @param ip Dirección ip del servidor en formato "127.0.0.1"
     * @param port Puerto donde esté escuchando el servidor.
     * @throws IOException En caso de no haber podido realizarse la conexión.
     */
    public DataBaseHashCliente(String ip, int port) throws IOException {
        this.conexionservidor = new HiloClienteServidor(ip, port, this);
        this.funcionhash = null;
        this.generador = null;
        this.sizehash = 0;
        reset();
        this.conexionservidor.start();
    }

    /**
     * Elimina la base de datos local y setea las banderas.
     */
    private void reset() {
        if (!(this.mensajes != null && this.mensajes.isEmpty())) {
            this.mensajes = Collections.synchronizedMap(new HashMap(MAXIMUM_CAPACITY / 2));
        }
        this.colisiones = new Colisiones();
        this.shutdown = false;
        this.activo = false;
        this.reset = false;
        this.nhashes = 0L;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado a " + conexionservidor.getConexionHost().toString());
        this.conexionservidor.registroServidor();
        try {
            while (!shutdown) {
                while (activo) {
                    Bytes newmensaje = new Bytes(generador.nextMensaje()); // Genera mensaje aleatorio.
                    funcionhash.engineReset();
                    funcionhash.engineUpdate(newmensaje.toByteArray(), 0, newmensaje.size()); //Alimenta algoritmo.
                    byte[] hasht = funcionhash.engineDigest(); //Calcula resumen.
                    hasht = reduceHash(hasht, sizehash); //Ajusta el tamaño del hash de salida.
                    Bytes hash = new Bytes(hasht); // Convierte en un objeto mapeable.
                    Bytes oldmensaje = mensajes.put(hash, newmensaje); // Almacena hash localmente
                    nhashes++;
                    if (oldmensaje != null) { // Si el hash ya estaba en la lista lo añade a colisiones.
                        colisiones.addColision(hash, newmensaje, oldmensaje);
                    }
                    if (!colisiones.isLastAddMsgRepeat()) {
                        conexionservidor.putHashServidor(hash); // Solo envia hash al servidor si no ha habido una pseudocolisión local.
                    }
                    if (!conexionservidor.isAlive() && !shutdown) {
                        System.out.println("Se ha perdido la conexión con el servidor.");
                        apagaCliente();
                    }
                }
                if (!conexionservidor.isAlive()){
                    break;
                }
                Thread.sleep(300);
                if (reset) {
                    reset();
                }
                Thread.sleep(300);
            }
        } catch (InterruptedException ex) {
            System.out.println("Cerrando Database Cliente: " + ex.getMessage());
        }
        liberaRecursos();
    }

    /**
     * Reduce el tamaño de bytes del código hash.
     *
     * @param hash Código hash completo.
     * @param sizehash Si es 0 o >= el tamaño completo, no cambia el tamaño.
     * @return Nuevo hash reducido.
     */
    private byte[] reduceHash(byte[] hash, int sizehash) {
        if (sizehash > 0 && sizehash < hash.length) {
            byte[] newhash = new byte[sizehash];
            System.arraycopy(hash, 0, newhash, 0, sizehash);
            return newhash;
        }
        return hash;
    }

    @Override
    public void setFuncionCliente(String algoritmo, int sizemsg, int sizehash) {
        this.funcionhash = FabricaHash.getInstance(algoritmo);
        this.generador = new GeneradorMensaje(sizemsg);
        this.sizehash = sizehash;
        System.out.println("Cliente configurado para generar mensajes aleatorios de "
                + (sizemsg * 8) + " bits.\nY calcular hash " + algoritmo
                + " de " + (sizehash * 8) + " bits.");
    }

    @Override
    public void startCliente() throws NoSetException {
        if (funcionhash == null || generador == null) {
            throw new NoSetException("El cliente no tiene la configuración correcta.");
        } else if (sizehash < 0) {
            throw new NoSetException("El cliente no tiene la configuración correcta.");
        }
        this.activo = true;
        System.out.println("Arrancando cliente.");
    }

    @Override
    public void stopCliente() {
        if (activo) {
            this.activo = false;
            System.out.println("Parando cliente.");
        }
    }

    @Override
    public void resetCliente() {
        this.reset = true;
        this.activo = false;
        System.out.println("Reset cliente.");
    }

    @Override
    public Bytes getMessageCliente(Bytes hash, boolean ultimo) {
        Bytes ultimohash, penultimohash;
        if (ultimo) {
            ultimohash = this.mensajes.get(hash);
            return ultimohash;
        } else {
            penultimohash = this.colisiones.getPenultimoMensaje(hash);
            return penultimohash;
        }
    }

    @Override
    public void apagaCliente() {
        if (conexionservidor.isAlive()) {
            conexionservidor.interrupt();
        }
        activo = false;        
        shutdown = true;
        System.out.println("Apagando cliente.");
        
    }

    /**
     * Libera los recursos utilizados por este objeto.
     */
    public void liberaRecursos() {
        System.out.println("Nº Hashes: " + this.nhashes + "  Nº colisiones: " + this.colisiones.getNgrupos() + "  Nº pseudocolisiones: " + this.colisiones.getPseudocolisiones());
        if (conexionservidor != null && conexionservidor.isAlive()) {
            conexionservidor.interrupt();
        }
    }

    @Override
    public long numeroHashes() {
        return nhashes;
    }

    @Override
    public int numeroColisiones() {
        return this.colisiones.getNgrupos();
    }

    @Override
    public int numeroPseudocolisiones() {
        return this.colisiones.getPseudocolisiones();
    }

    @Override
    public String getInformeColisiones() {
        return this.colisiones.getInformeColisiones();
    }

}

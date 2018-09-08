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
package criptolabhash.ataques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Almacen donde se realiza el mantenimiento de las colisiones.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class Colisiones {

    /**
     * Almacena los hashes asociados con un conjunto de mensajes que los
     * producen. Emlpeamos un List en lugar de un Set, porque precisamente Set
     * se implementa con HashSet y si utiliza el mismo algoritmo hash que el de
     * la colisión, no permitiría añadir mas mensajes al conjunto.
     */
    private final Map<Bytes, List<Bytes>> colisiones;

    /**
     * Número de colisiones por mensajes repetidos.
     */
    private volatile int pseudocolisiones;

    /**
     * Indica si el último newmensaje ya estaba en alguna lista.
     */
    private volatile boolean mensajerepetido;

    public Colisiones() {
        this.colisiones = Collections.synchronizedMap(new HashMap());
        this.pseudocolisiones = 0;
    }

    /**
     * Añade mensajes nuevos que producen un mismo hash.
     *
     * @param hash Hash que producen los mensajes.
     * @param newmensaje Mensaje candidato a colisión.
     * @param oldmensaje Mensaje que ya se había generado anteriormente.
     * @return True si se ha identificado un nuevo grupo de colisión, o si
     * alguno de los mensajes no estaba en la lista de algún grupo ya
     * establecido.
     */
    public synchronized boolean addColision(Bytes hash, Bytes newmensaje, Bytes oldmensaje) {
        mensajerepetido = false;
        if (hash == null) {
            return false;
        }
        if (colisiones.containsKey(hash)) {
            List<Bytes> listacolisiones = colisiones.get(hash);
            boolean insercion = false;
            if (newmensaje != null) {
                if (!listacolisiones.contains(newmensaje)) {
                    listacolisiones.add(newmensaje);
                    insercion = true;
                } else {
                    pseudocolisiones++;
                    mensajerepetido = true;
                }
            }
            if (oldmensaje != null) {
                if (!listacolisiones.contains(oldmensaje)) {
                    listacolisiones.add(oldmensaje);
                    insercion = true;
                }
            }
            return insercion;
        } else if (newmensaje != null && oldmensaje != null && !newmensaje.equals(oldmensaje)) {
            List<Bytes> colision = new ArrayList();
            colision.add(oldmensaje);
            colision.add(newmensaje);
            colisiones.put(hash, colision);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Número de grupos de colisiones almacenadas.
     *
     * @return Número de grupos.
     */
    public int getNgrupos() {
        return this.colisiones.size();
    }

    /**
     * Número de colisiones falsas, por mensajes repetidos.
     *
     * @return Cantidad de veces.
     */
    public int getPseudocolisiones() {
        return this.pseudocolisiones;
    }

    /**
     * Indica si el último mensaje añadido estaba ya anteriormrnte en la lista.
     * Despues de esta consulta el valorse retorna a false, de esta forma si se
     * vuelve a consultar antes de que haya una nueva inserción, dara como
     * resultado false.
     *
     * @return true si ya habia el nuevo mensaje.
     */
    public boolean isLastAddMsgRepeat() {
        boolean repetido = mensajerepetido;
        mensajerepetido = false;
        return repetido;
    }

    /**
     * Devuelve la lista de los mensajes que generan un hash.
     *
     * @param hash Hash generado por la lista de mensajes que se busca.
     * @return Lista de mensajes.
     */
    public List<Bytes> getMensajes(Bytes hash) {
        return colisiones.get(hash);
    }

    /**
     * Devuelve el último mensaje que ha generado el hash.
     *
     * @param hash Hash del mensaje que se busca.
     * @return Mensaje que generó ese hash.
     */
    public Bytes getMensaje(Bytes hash) {
        List<Bytes> listamensajes = colisiones.get(hash);
        if (listamensajes == null || listamensajes.size() < 1) {
            return null;
        }
        return listamensajes.get(listamensajes.size() - 1);
    }

    /**
     * Devuelve el penúltimo mensaje almacenado para este hash.
     *
     * @param hash Hash del mensaje que se busca.
     * @return Mensaje que generó ese hash. Null si no existe grupo asociado al
     * hash.
     */
    public Bytes getPenultimoMensaje(Bytes hash) {
        List<Bytes> listamensajes = colisiones.get(hash);
        if (listamensajes == null || listamensajes.size() < 2) {
            return null;
        }
        return listamensajes.get(listamensajes.size() - 2);
    }

    /**
     * Genera un informe con los mensajes encontrados que comparten un mismo
     * código hash, agrupados por el mismo hash.
     *
     * @return Informe en texto plano de 64 carácteres de ancho.
     */
    public String getInformeColisiones() {
        StringBuilder informe = new StringBuilder("");
        int ngrupo = 0;
        for (Map.Entry<Bytes, List<Bytes>> entrada : colisiones.entrySet()) {
            Bytes hash = entrada.getKey();
            List<Bytes> mensajes = entrada.getValue();
            informe.append("----------------------------------------------------------------\n"
                    + "Grupo " + ++ngrupo + " de colisiones encontradas para el Hash:\n"
                    + hash.toHexString() + "\n"
                    + "----------------------------------------------------------------\n");
            int nmensaje = 0;
            Iterator<Bytes> lista = mensajes.iterator();
            while (lista.hasNext()) {
                informe.append("Mensaje " + ++nmensaje + "\n"
                        + "----------------------------------------------------------------\n");
                informe.append(lista.next().toHexString(64) + "\n");
            }
            informe.append("\n");
        }
        return informe.toString();
    }

}

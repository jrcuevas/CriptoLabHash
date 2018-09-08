/*
 * Copyright (C) 2018 José Ramón Cuevas  <www.linkedin.com/in/joseramoncuevasdiez>
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

import java.io.Serializable;

/**
 * Mensajes utilizados para comunicarse entre cliente y servidor. Los nombres de
 * las respuestas a un mensaje que contengan datos, deberán llevar el sufijo
 * ACK.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class Mensaje implements Serializable {

    private final TMensaje comando;
    private String algoritmo;
    private Integer sizemsg;
    private Integer sizehash;
    private Long secuencia;
    private Bytes mensaje;

    /**
     * Setea el tipo de Mensaje y marca la secuencia con un indicador de tiempo.
     *
     * @param comando Tipo de mensaje.
     */
    public Mensaje(TMensaje comando) {
        this.comando = comando;
        this.algoritmo = null;
        this.sizemsg = null;
        this.sizehash = null;
        this.mensaje = null;
        this.secuencia = Secuencia.nextnumber();
    }

    /**
     * Indica un nombre de algoritmo.
     *
     * @param algoritmo Nombre del algoritmo.
     * @param sizemsg Tamaño del mensaje en bytes.
     * @param sizehash Tamaño del hash calculado en bytes.
     * @return El propio mensaje.
     */
    public Mensaje setAlgoritmo(String algoritmo, int sizemsg, int sizehash) {
        this.algoritmo = algoritmo;
        this.sizemsg = sizemsg;
        this.sizehash = sizehash;
        return this;
    }

    /**
     * Setea el valor de la secuencia del mensaje.
     *
     * @param secuencia secuencia del mensaje.
     * @return El propio mensaje.
     */
    public Mensaje setSecuencia(long secuencia) {
        this.secuencia = secuencia;
        return this;
    }

    /**
     * Carga el contenido del mensaje en bytes.
     *
     * @param mensaje Mensaje o Hash.
     * @return El propio mensaje.
     */
    public Mensaje setMensaje(Bytes mensaje) {
        this.mensaje = mensaje;
        return this;
    }

    /**
     * Devuelve el valor del algorimo si ha sido cargado.
     *
     * @return Nombre del algoritmo.
     */
    public String getAlgoritmo() {
        return this.algoritmo;
    }

    /**
     * Devuelve el valor de la sizemsg si ha sido cargada.
     *
     * @return Tamaño de mensaje a generar.
     */
    public int getSizeMsg() {
        return this.sizemsg;
    }

    /**
     * Devuelve el valor de la sizehash si ha sido cargada.
     *
     * @return Tamaño de hash a calcular.
     */
    public int getSizeHash() {
        return this.sizehash;
    }

    /**
     * Devuelve el valor de la sizemsg si ha sido cargada.
     *
     * @return Semilla a utilizar.
     */
    public long getSecuencia() {
        return this.secuencia;
    }

    /**
     * Devuelve el valor del mensaje si ha sido cargado.
     *
     * @return Mensaje o Hash.
     */
    public Bytes getMensaje() {
        return this.mensaje;
    }

    @Override
    public String toString() {
        return this.comando.toString();
    }
}

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
 *
 * @author José Ramón Cuevas  <www.linkedin.com/in/joseramoncuevasdiez>
 */
public enum Mensaje implements Serializable {

    ACK("ACK"),
    REGISTRO("REGISTRO"),
    START("START"),
    STOP("STOP"),
    RESET("RESET"),
    SET("SET"),
    GETMESSAGE("GETMESSAJE"),
    PUTHASH("PUTHASH");

    private final String comando;
    private String algoritmo;
    private Long semilla;
    private Byte[] mensaje;

    /**
     * Setea el tipo de Mensaje.
     *
     * @param comando
     */
    Mensaje(String comando) {
        this.comando = comando;
        this.algoritmo = null;
        this.semilla = null;
        this.mensaje = null;
    }

    /**
     * Indica un nombre de algoritmo.
     *
     * @param algoritmo
     */
    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    /**
     * Indica el valor de la semilla.
     *
     * @param semilla
     */
    public void setSemilla(long semilla) {
        this.semilla = semilla;
    }

    /**
     * Carga el contenido del mensaje en bytes.
     *
     * @param mensaje
     */
    public void setMensaje(Byte[] mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Devuelve el valor del algorimo si ha sido cargado.
     *
     * @return
     */
    public String getAlgoritmo() {
        return this.algoritmo;
    }

    /**
     * Devuelve el valor de la semilla si ha sido cargada.
     *
     * @return
     */
    public Long getSemilla() {
        return this.semilla;
    }

    /**
     * Devuelve el valor del mensaje si ha sido cargado.
     *
     * @return
     */
    public Byte[] getMensaje() {
        return this.mensaje;
    }

    @Override
    public String toString() {
        return this.comando;
    }
}

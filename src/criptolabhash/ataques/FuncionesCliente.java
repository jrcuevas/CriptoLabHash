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

/**
 * Funciones que debe disponer el cliente
 *
 * @author José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface FuncionesCliente {

    /**
     * Setea la función a utilizar y establece una semilla para la exploración.
     * Primero para la ejecución actual luego efectua
     *
     * @param algoritmo Nombre del algoritmo a utilizar.
     * @param semilla Semilla con la que se generarán los mensajes pseudoaleatorios.
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public void setFuncion(String algoritmo, long semilla) throws NullPointerException;

    /**
     * Comienza o reanuda la ejecución con los valores seteados.
     *
     * @throws NullPointerException En caso de algún problema de comunicación.
     * @throws NoSetException No se ha configurado el algoritmo a usar.
     */
    public void start() throws NullPointerException, NoSetException;

    /**
     * Para la ejecución actual.
     *
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public void stop() throws NullPointerException;

    /**
     * Para la ejecución actual y elimina el mapa de los códigos explorados
     *
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public void reset() throws NullPointerException;

    /**
     * Consigue el mensaje que generó el hash facilitado.
     *
     * @param hash Hash que ha sido generado anteriormente.
     * @return Mensaje solicitado.
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public Byte[] getMessage(Byte[] hash) throws NullPointerException;

}

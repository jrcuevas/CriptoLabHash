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
package criptolabhash.ataques.servidor;

import criptolabhash.ataques.Bytes;
import criptolabhash.ataques.NoSetException;

/**
 * Funciones que debe disponer el cliente
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface FuncionesCliente {

    /**
     * Setea la función a utilizar y establece una semilla para la exploración.
     * Primero para la ejecución actual luego efectua
     *
     * @param algoritmo Nombre del algoritmo a utilizar.
     * @param sizemsg Longitud en bytes con la que se generarán los mensajes
     * pseudoaleatorios.
     * @param sizehash Longitud en bytes con la que se calcularan los hash.
     * (Valores válidos 0 = tamaño original del algoritmo, o mayor que 0)
     */
    public void setFuncionCliente(String algoritmo, int sizemsg, int sizehash);

    /**
     * Comienza o reanuda la ejecución con los valores seteados.
     *
     * @throws NoSetException No se ha configurado el algoritmo a usar.
     */
    public void startCliente() throws NoSetException;

    /**
     * Para la ejecución actual.
     *
     */
    public void stopCliente();

    /**
     * Para la ejecución actual y elimina el mapa de los códigos explorados
     *
     */
    public void resetCliente();

    /**
     * Consigue el mensaje que generó el hash facilitado.
     *
     * @param hash Hash que ha sido generado anteriormente.
     * @param ultimo Si se quiere que sea el último poner a true;
     * @return Mensaje solicitado.
     */
    public Bytes getMessageCliente(Bytes hash, boolean ultimo);

    /**
     * Envía un mensaje al cliente para que salga de ejecución y termina la
     * ejecución de este hilo.
     *
     */
    public void apagaCliente();
}

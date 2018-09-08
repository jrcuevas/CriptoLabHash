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

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public enum TMensaje {
    ACK("ACK"), // Respuesta de confirmación
    NOACK("NOACK"), //Respuesta erronea
    REGISTRO("REGISTRO"), // Registro del cliente
    START("START"), // Comienzo de exploración
    STOP("STOP"), // Parada de exploración
    RESET("RESET"), // Borrado de mensajes y parada del cliente
    SHUTDOWN("SHUTDOWN"), // Apagado del cliente
    SETFUNCION("SETFUNCION"), // Seteo de parametros
    GETMESSAGE("GETMESSAJE"), // Petición de mensaje correspondiente a un hash
    GETMESSAGEACK("GETMESSAJEACK"),// Respuesta a la petición del mensaje generado por un hash
    PUTHASH("PUTHASH");         // Introduce hash generado en el cliente

    /**
     * Tipo de mensaje
     */
    private final String comando;

    TMensaje(String comando) {
        this.comando = comando;
    }

}

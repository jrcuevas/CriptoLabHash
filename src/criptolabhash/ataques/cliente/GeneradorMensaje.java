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

import java.util.Random;

/**
 * Esta clase se ocupa de generar mensajes de forma aleatoria.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class GeneradorMensaje {

    /**
     * Generador aleatorio.
     */
    private Random generador;

    /**
     * Longitud en bytes de los mensajes generados
     */
    private final int longitud;

    /**
     * Prepara el generador con una semilla tomada del reloj.
     *
     * @param longitud Longitud de los mensajes generados.
     */
    public GeneradorMensaje(int longitud) {
        this.longitud = longitud;
        this.generador = new Random(System.currentTimeMillis());
    }

    /**
     * Genera un nuevo mensaje aleatorio.
     *
     * @return Un array de bytes conteniendo el mensaje aleatorio.
     */
    public byte[] nextMensaje() {
        byte[] mensaje = new byte[longitud];
        this.generador.nextBytes(mensaje);
        return mensaje;
    }
}

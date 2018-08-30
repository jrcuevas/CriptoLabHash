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
 * Funciones que debe tener el servidor
 *
 * @author José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface FuncionesServidor {

    /**
     * Registro del cliente en el servidor.
     *
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public void registro() throws NullPointerException;

    /**
     * Añadir hash al mapa del servidor.
     *
     * @param hash Hash generado a partir de un mensaje pseudoaleatorio.
     * @throws NullPointerException En caso de algún problema de comunicación.
     */
    public void putHash(Byte[] hash) throws NullPointerException;

}

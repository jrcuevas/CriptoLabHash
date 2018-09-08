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
package criptolabhash.control;

/**
 * Operaciones de consulta que se pueden realizar con el cliente que genera los
 * códigos hash.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface APIClienteHash {

    /**
     * Consulta el número de Hashes calculados hasta el momento.
     *
     * @return Número de Hashes.
     */
    public long numeroHashes();

    /**
     * Consulta el número de grupo de colisiones encontradas hasta ahora.
     *
     * @return Número de colisiones.
     */
    public int numeroColisiones();

    /**
     * Consulta el número de colisiones encontradas que no son reales, porque
     * los dos mensajes que generan el mismo hash son iguales.
     *
     * @return Número de pseudocolisiones.
     */
    public int numeroPseudocolisiones();

    /**
     * Genera un informe con todos los hash encontrados, que son generados por
     * dos o más mensajes.
     *
     * @return Informe en formato texto plano de 64 caracteres de ancho.
     */
    public String getInformeColisiones();
}

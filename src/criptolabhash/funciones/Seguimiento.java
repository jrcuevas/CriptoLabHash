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
package criptolabhash.funciones;


/**
 * Esta clase abstracta, extiende las características de MessageDigestSpi, para
 * que las clases que heredan incluyan el seguimiento.
 *
 * @author José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface Seguimiento {

    /**
     * Retorna el seguimiento de la función hash. Si la función no
     * ha terminado, con esta llamada se procesan los datos actuales y se
     * calculará el hash. Ya no se admitirán mas datos.
     *
     * @return Texto con la explicación de los pasos por bloque realizados.
     */
    public String getSeguimiento();
}

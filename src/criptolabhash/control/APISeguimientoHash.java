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

import java.io.File;
import java.io.IOException;

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public interface APISeguimientoHash {

    /**
     * Procesa el fichero facilitado.
     *
     * @param algoritmo Algoritmo que se utilizará.
     * @param nivel Nivel de detalle del informe. nulo sin detalle. True paso a
     * paso. False a nivel de bloque.
     * @param fichero Fichero del que se quiere calcular el hash.
     * @param informe Informe de sguimiento.
     * @return Hash en representación hexadecimal.
     * @throws IOException En caso de tener problemas al acceder al archivo.
     */
    public String procesaFile(String algoritmo, Boolean nivel, File fichero, StringBuilder informe) throws IOException;

    /**
     * Procesa el texto facilitado.
     *
     * @param algoritmo Algoritmo que se utilizará.
     * @param nivel Nivel de detalle del informe. nulo sin detalle. True paso a
     * paso. False a nivel de bloque.
     * @param texto Mensaje de texto del que se quiere calcular el hash.
     * @param informe Informe de sguimiento.
     * @return Hash en representación hexadecimal.
     */
    public String procesaText(String algoritmo, Boolean nivel, String texto, StringBuilder informe);

    /**
     * Procesa el mensaje en hexadecimal facilitado.
     *
     * @param algoritmo Algoritmo que se utilizará.
     * @param nivel Nivel de detalle del informe. nulo sin detalle. True paso a
     * paso. False a nivel de bloque.
     * @param hexadecimal Mensaje en hexadecimal del que se quiere calcular el
     * hash.
     * @param informe Informe de sguimiento.
     * @return Hash en representación hexadecimal.
     */
    public String procesaHex(String algoritmo, Boolean nivel, String hexadecimal, StringBuilder informe) throws IllegalArgumentException;
}

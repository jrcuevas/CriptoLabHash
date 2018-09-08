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

import java.io.Serializable;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;

/**
 * Clase utilizada para poder utilizar arrays de byte en un HashMap
 * 
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class Bytes implements Serializable{

    private final byte[] bytes;

    public Bytes(byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.bytes = data;
    }
    
    /**
     * Retorna el valor representado en Hexadecimal
     * @return Representación en hexadecimal.
     */
    public String toHexString(){
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    /**
     * Retorna el valor representado en Hexadecimal,
     * con el ancho de linea indicado.
     * @param ancho Número de caracteres por linea.
     * @return Valor hexadecimal.
     */
    public String toHexString(int ancho){
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(DatatypeConverter.printHexBinary(bytes));
        int index = 0;
        while ((index += ancho) < mensaje.length()){
            mensaje.insert(index, "\n");
            index++;
        }
        mensaje.append("\n");
        return mensaje.toString();
    }
    
    /**
     * Devuelve el array de bytes contenido.
     * @return Array de bytes.
     */
    public byte[] toByteArray(){
        return bytes;
    }
    
    /**
     * Número de bytes
     * @return Número de bytes.
     */
    public int size(){
        return bytes.length;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Bytes)) {
            return false;
        }
        return Arrays.equals(bytes, ((Bytes) other).bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}

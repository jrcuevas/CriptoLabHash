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
package criptolabhash.funciones;

/**
 * Fábrica de instancias de funciones hash.
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class FabricaHash {
    
    /**
     * Fabrica de instancias hash.
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public static Hash32 getInstance(String algoritmo){
        switch(algoritmo){
            case "MD5":
                return new MD5();
            case "SHA1":
                return new SHA1();
            default:
                return null;
        }
    }
    
    /**
     * Fabrica de instancias hash con seguimiento pasoapaso.
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public static Hash32 getInstancePasoapaso(String algoritmo){
        switch(algoritmo){
            case "MD5":
                return new MD5(true);
            case "SHA1":
                return new SHA1(true);
            default:
                return null;
        }
    }
    
    /**
     * Fabrica de instancias hash con seguimiento por bloques.
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public static Hash32 getInstancePorbloque(String algoritmo){
        switch(algoritmo){
            case "MD5":
                return new MD5(false);
            case "SHA1":
                return new SHA1(false);
            default:
                return null;
        }
    }
}

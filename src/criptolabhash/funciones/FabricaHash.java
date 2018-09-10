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
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class FabricaHash {

    // Lista de algoritmos que instanciará esta fábrica.
    public final static String MD5 = "MD5";
    public final static String SHA1 = "SHA1";

    /**
     * Mapeo de algoritmos que puede instanciar esta fábrica y la longitud en
     * bytes del resumen hash.
     */
    private final static String[][] ALGORITMOS = new String[][]{{MD5, "16"}, {SHA1, "20"}};

    /**
     * Devuelve un array con la lista de algoritmos disponibles.
     *
     * @return Array de nombres de algoritmos.
     */
    public final static String[] arrayAlgoritmos() {
        String[] algoritmos = new String[ALGORITMOS.length];
        for (int index = 0; index < ALGORITMOS.length; index++) {
            algoritmos[index] = ALGORITMOS[index][0];
        }
        return algoritmos;
    }

    /**
     * Devuelve la longitud en bytes que posee el resumen de salida del
     * algoritmo indicado.
     *
     * @param algoritmo Nombre del algoritmo.
     * @return Longitud en bytes.
     */
    public final static int longitudHash(String algoritmo) {
        int index = 0;
        while (index < ALGORITMOS.length) {
            if (ALGORITMOS[index][0].equals(algoritmo)) {
                return Integer.parseInt(ALGORITMOS[index][1]);
            }
        }
        return 0;
    }

    /**
     * Fabrica de instancias hash.
     *
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public final static Hash32 getInstance(String algoritmo) {
        switch (algoritmo) {
            case MD5:
                return new MD5();
            case SHA1:
                return new SHA1();
            default:
                return null;
        }
    }

    /**
     * Fabrica de instancias hash con seguimiento pasoapaso.
     *
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public final static Hash32 getInstancePasoapaso(String algoritmo) {
        switch (algoritmo) {
            case MD5:
                return new MD5(true);
            case SHA1:
                return new SHA1(true);
            default:
                return null;
        }
    }

    /**
     * Fabrica de instancias hash con seguimiento por bloques.
     *
     * @param algoritmo Nombre del algoritmo.
     * @return Instancia del algoritmo deseado.
     */
    public final static Hash32 getInstancePorbloque(String algoritmo) {
        switch (algoritmo) {
            case MD5:
                return new MD5(false);
            case SHA1:
                return new SHA1(false);
            default:
                return null;
        }
    }
}

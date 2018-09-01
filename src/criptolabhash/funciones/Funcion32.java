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

import java.security.MessageDigestSpi;
import javax.xml.bind.DatatypeConverter;

/**
 * Operaciones de 32 bit que se utilizan normalmente en los algoritmos hash.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public abstract class Funcion32 extends MessageDigestSpi {

    /**
     * Bloque de datos a tratar. Contiene 16 palabras de 32 bits.
     */
    protected int[] bloque = new int[16];

    /**
     * Indice que apunta al próximo byte a cargar en el bloque, el máximo son 64
     * bytes.
     */
    protected byte index;

    /**
     * Tamaño en bits del mensaje.
     */
    protected long size;

    /**
     * Almacena el vector A.
     */
    protected int A;

    /**
     * Almacena el vector B.
     */
    protected int B;

    /**
     * Almacena el vector C.
     */
    protected int C;

    /**
     * Almacena el vector D.
     */
    protected int D;
    
    /**
     * Indica si ha terminado.
     */
    protected boolean finished;

    /**
     * Rotación a la izquierda del número de bits indicado.
     *
     * @param palabra Palabra de 32 bit de entrada.
     * @param desplazamiento Número de bits que será rotado.
     * @return El resultado de la operación.
     */
    protected int RL(int palabra, byte desplazamiento) {
        return (palabra << desplazamiento) | (palabra >>> (32 - desplazamiento));
    }

    /**
     * Rotación a la derecha del número de bits indicado.
     *
     * @param palabra Palabra de 32 bit de entrada.
     * @param desplazamiento Número de bits que será rotado.
     * @return El resultado de la operación.
     */    
    protected int RR(int palabra, byte desplazamiento) {
        return (palabra >>> desplazamiento) | (palabra << (32 - desplazamiento));
    }

    /**
     * Suma de complemento a dos.
     *
     * @param palabraA Palabra de 32 bit de entrada, operando A.
     * @param palabraB Palabra de 32 bit de entrada, operando B.
     * @return El resultado de la operación.
     */
    protected int SUM(int palabraA, int palabraB) {
        //return (int)((long)palabraA + (long)palabraB) & 0xffffffff;
        return (int) ((long) palabraA + (long) palabraB);
    }

    /**
     * Cambia el modo de representación, de little endian a big endian y
     * viceversa.
     *
     * @param palabra Palabra de 32 bit de entrada.
     * @return El resultado de la operación.
     */
    protected int switchEndian(int palabra) {
        int resultado = palabra >>> 24;
        resultado |= (palabra & 0xff0000) >>> 8;
        resultado |= (palabra & 0xff00) << 8;
        resultado |= palabra << 24;
        return resultado;
    }

    /**
     * Añade un bit de relleno a "1" y el resto a "0" hasta llenar los 56 bytes
     * primeros, seguidamente añade la longitud. Si los 56 bytes primeros ya
     * estuvieran ocupados, se rellena un bloque mas.
     */
    protected void addRelleno() {
        int pos = addByteLittle((byte) 0b10000000);
        while (pos != 56) {
            pos = addByteLittle((byte) 0b00000000);
        }
        long tamano = size;
        for (byte ind = 8; ind > 0; ind--) {
            addByteLittle((byte) (tamano & 0xff));
            tamano >>>= 8;
        }
        this.finished = true;
    }

    /**
     * Añade un nuevo byte al bloque en formato little endian y en palabras de
     * 32 bits. Incrementa el puntero de bytes y cuando se llena el bloque
     * vuelve a 0 y procesa el bloque.
     *
     * @param mensaje Byte de información del mensaje.
     * @return El lugar del puntero. Si es 0 induca que el bloque esta lleno.
     */
    protected int addByteLittle(byte mensaje) {
        byte pos = (byte) (index & 0b00000011);
        byte ind = (byte) (index >>> 2);
        int dato = mensaje & 0xff;
        switch (pos) {
            case 0:
                bloque[ind] |= dato;
                break;
            case 1:
                bloque[ind] |= dato << 8;
                break;
            case 2:
                bloque[ind] |= dato << 16;
                break;
            case 3:
                bloque[ind] |= dato << 24;
                break;
        }
        index++;
        if (index > 63) {
            index = 0;
            procesaBloque();
            // Resetea bloque.
            for (int index = 0; index < bloque.length; index++) {
                bloque[index] = 0;
            }
        }
        return index;
    }

    /**
     * Convierte un int en un array de bytes.
     *
     * @param word palabra a convertir.
     * @return Un array de bytes con el contenido del int.
     */
    protected byte[] toBytes(int word) {
        byte[] bytes = new byte[4];
        for (byte ind = 3; ind >= 0; ind--) {
            bytes[ind] = (byte) (word & 0xff);
            word >>>= 8;
        }
        return bytes;
    }

    /**
     * Realiza las vueltas de proceso del bloque.
     */
    protected abstract void procesaBloque();

    /**
     * Retorna una linea con una representación hexadecimal de los cuatro
     * valores.
     *
     * @param valores Valores a imprimir en hexadecimal.
     * @return Una linea con los cuatro valores.
     */
    protected String bufferesToStringH(int[] valores) {
        String salida = "";
        for (int valor : valores) {
            salida += DatatypeConverter.printHexBinary(toBytes(valor)) + " ";
        }
        salida += "\n";
        return salida;
    }

    /**
     * Imprime en vertical los buferes en representación binaria y hexadecimal.
     *
     * @param valores Valores a imprimir.
     * @param indicadores Nombre de los valores.
     * @return Representación vertical de los vectores.
     */
    protected String bufferesToStringV(int[] valores, char[] indicadores) {
        String salida = "";
        for (int index = 0; index < valores.length; index++) {
            salida += indicadores[index] + " = "
                    + intToBinaryString(valores[index]) + " "
                    + DatatypeConverter.printHexBinary(toBytes(valores[index])) + "\n";
        }
        return salida;
    }

    /**
     * Imprime una representación del bloque que se va a procesar.
     *
     * @return Representación en binario y hexadecimal.
     */
    protected String bloqueToString() {
        String salida = "";
        for (int index = 0; index < bloque.length; index++) {
            salida += String.format("Palabra %2d ", index);
            salida += intToBinaryString(bloque[index]) + " ";
            salida += "= " + DatatypeConverter.printHexBinary(toBytes(bloque[index])) + "\n";
        }
        salida += "\n";
        return salida;
    }

    /**
     * Convierte un entero a su representación binaria.
     *
     * @param palabra Palabra que se quiere representar en binario.
     * @return Representación binaria.
     */
    protected String intToBinaryString(int palabra) {
        char[] buffer = new char[32];
        int bit = 0b10000000000000000000000000000000;
        for (int index = 0; index < 32; index++) {
            buffer[index] = ((palabra & bit) == 0) ? '0' : '1';
            bit >>>= 1;
        }
        return new String(buffer);
    }

    /**
     * Calcula el número de bloque actual.
     *
     * @return Número de bloque.
     */
    protected long nBloque() {
        return size / 512 + ((size % 512 > 0) ? 1 : 0);
    }
}

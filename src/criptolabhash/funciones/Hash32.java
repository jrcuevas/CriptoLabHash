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
public abstract class Hash32 extends MessageDigestSpi implements Seguimiento {

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
     * Almacena el vector E.
     */
    protected int E;

    /**
     * Resumen procesado, si null no ha terminado.
     */
    protected byte[] resumen;

    /**
     * Seguimiento del algoritmo.
     */
    protected String track;

    /**
     * Operación y resultado de la última función realizada.
     */
    protected String[] opfuncion;

    /**
     * Seguimiento paso a paso?.
     */
    protected boolean pasoapaso;

    /**
     * Seguimiento por bloque?.
     */
    protected boolean porbloque;

    /**
     * Nombre del algoritmo.
     */
    protected String nombre;

    /**
     * Operaciones obligatorias para instanciación de los algoritmos Hash sin
     * seguimiento.
     *
     * @param nombre Nombre del algoritmo.
     */
    protected Hash32(String nombre) {
        this.nombre = nombre;
        this.opfuncion = new String[2];
        this.engineReset();
    }

    /**
     * Operaciones obligatorias a realizar para instanciación con seguimiento.
     *
     * @param nombre Nombre del algoritmo.
     * @param pasoapaso true para realizar un seguimiento paso a paso.
     */
    protected Hash32(String nombre, boolean pasoapaso) {
        this.nombre = nombre;
        this.opfuncion = new String[2];
        if (pasoapaso) {
            this.pasoapaso = true;
        } else {
            this.porbloque = true;
        }
        this.engineReset();
    }

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
    protected void addRellenoBig() {
        int pos = addByteBig((byte) 0b10000000);
        while (pos != 56) {
            pos = addByteBig((byte) 0b00000000);
        }
        long tamano = size;
        byte[] tamb = new byte[8];
        for (byte ind = 7; ind >= 0; ind--) {
            tamb[ind] = (byte) (tamano & 0xff);
            tamano >>>= 8;
        }
        for (byte Byte : tamb) {
            addByteBig(Byte);
        }
    }

    /**
     * Añade un bit de relleno a "1" y el resto a "0" hasta llenar los 56 bytes
     * primeros, seguidamente añade la longitud. Si los 56 bytes primeros ya
     * estuvieran ocupados, se rellena un bloque mas.
     */
    protected void addRellenoLittle() {
        int pos = addByteLittle((byte) 0b10000000);
        while (pos != 56) {
            pos = addByteLittle((byte) 0b00000000);
        }
        long tamano = size;
        for (byte ind = 8; ind > 0; ind--) {
            addByteLittle((byte) (tamano & 0xff));
            tamano >>>= 8;
        }
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
     * Añade un nuevo byte al bloque en formato big endian y en palabras de 32
     * bits. Incrementa el puntero de bytes y cuando se llena el bloque vuelve a
     * 0 y procesa el bloque.
     *
     * @param mensaje Byte de información del mensaje.
     * @return El lugar del puntero. Si es 0 induca que el bloque esta lleno.
     */
    protected int addByteBig(byte mensaje) {
        byte pos = (byte) (index & 0b00000011);
        byte ind = (byte) (index >>> 2);
        int dato = mensaje & 0xff;
        switch (pos) {
            case 0:
                bloque[ind] |= dato << 24;
                break;
            case 1:
                bloque[ind] |= dato << 16;
                break;
            case 2:
                bloque[ind] |= dato << 8;
                break;
            case 3:
                bloque[ind] |= dato;
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
    protected byte[] intToBytes(int word) {
        byte[] bytes = new byte[4];
        for (byte ind = 3; ind >= 0; ind--) {
            bytes[ind] = (byte) (word & 0xff);
            word >>>= 8;
        }
        return bytes;
    }

    /**
     * Convierte un array de int en un array de bytes.
     *
     * @param words Array que contiene los ints a convertir.
     * @return Un array de bytes con el contenido del int[].
     */
    protected byte[] intToBytes(int[] words) {
        byte[] bytes = new byte[words.length * 4];
        for (int ind = 0; ind < words.length; ind++) {
            byte[] hex = intToBytes(words[ind]);
            for (int ind1 = 0; ind1 < hex.length; ind1++) {
                bytes[ind * hex.length + ind1] = hex[ind1];
            }
        }
        return bytes;
    }

    /**
     * Realiza las vueltas de proceso del bloque.
     */
    protected abstract void procesaBloque();

    /**
     * Retorna una linea con una representación hexadecimal de los cuatro
     * valores separados por un espacio.
     *
     * @param valores Valores a imprimir en hexadecimal.
     * @return Una linea con los cuatro valores.
     */
    protected String bufferesToStringH(int[] valores) {
        String salida = "";
        for (int valor : valores) {
            salida += intToHexString(valor) + " ";
        }
        salida += "\n";
        return salida;
    }

    /**
     * Retorna una linea con una representación hexadecimal de los cuatro
     * valores.
     *
     * @param valores Valores a imprimir en hexadecimal.
     * @return Una linea con los valores concatenados.
     */
    protected String resumenToString(int[] valores) {
        String salida = "";
        for (int valor : valores) {
            salida += intToHexString(valor);
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
                    + intToHexString(valores[index]) + "\n";
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
            salida += "= " + intToHexString(bloque[index]) + "\n";
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
     * Convierte una palabra int en su representación Hexadecimal.
     *
     * @param palabra Palabra a representar.
     * @return Un string conteniendo el valor hexadecimal.
     */
    protected String intToHexString(int palabra) {
        return DatatypeConverter.printHexBinary(intToBytes(palabra));
    }

    /**
     * Calcula el número de bloque actual.
     *
     * @return Número de bloque.
     */
    protected long nBloque() {
        return (size / 512) + ((size % 512 > 0) ? 1 : 0);
    }

    /**
     * Obtiene el informe de seguimiento
     *
     * @return Informe de las operaciones realizadas.
     */
    public final String getSeguimiento() {
        if (resumen == null) {
            this.engineDigest();
        }
        if (track.equals("")) {
            return "No se ha realizado ningún tipo de seguimiento.";
        }
        return this.track;
    }

    @Override
    public abstract byte[] engineDigest();

    @Override
    public abstract void engineUpdate(byte input);

    @Override
    public void engineUpdate(byte[] input, int offset, int len) {
        int end = offset + len;
        for (int index = offset; index < input.length && index < end; index++) {
            engineUpdate(input[index]);
        }
    }

    @Override
    public void engineReset() {
        index = 0;
        size = 0;
        resumen = null;
        track = "";
        for (int index = 0; index < opfuncion.length; index++) {
            opfuncion[index] = "";
        }
    }

    /**
     * Retorna el nombre de la función hash.
     *
     * @return nombre que identifica a la función.
     */
    public String getNombre() {
        return this.nombre;
    }
}

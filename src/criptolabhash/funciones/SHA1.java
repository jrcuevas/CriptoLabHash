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
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class SHA1 extends Hash32 implements Seguimiento {

    private static final int Ainicial = 0x67452301;
    private static final int Binicial = 0xefcdab89;
    private static final int Cinicial = 0x98badcfe;
    private static final int Dinicial = 0x10325476;
    private static final int Einicial = 0xc3d2e1f0;
    private static final int K1 = 0x5a827999;
    private static final int K2 = 0x6ed9eba1;
    private static final int K3 = 0x8f1bbcdc;
    private static final int K4 = 0xca62c1d6;

    /**
     * Bloque de datos a tratar. Contiene 80 palabras de 32 bits.
     */
    protected int[] W = new int[80];

    /**
     * Instanciación sin seguimiento.
     */
    public SHA1() {
        super();
    }

    /**
     * Instanciación con seguimiento.
     *
     * @param pasoapaso Indica si se quiere obtener seguimiento paso a paso o
     * por bloques.
     */
    public SHA1(boolean pasoapaso) {
        super(pasoapaso);
    }

    /**
     * Operación F
     *
     * @param b Palabra de 32 bit de entrada.
     * @param c Palabra de 32 bit de entrada.
     * @param d Palabra de 32 bit de entrada.
     * @return El resultado de la operación.
     */
    private int F(int b, int c, int d) {
        int bc = b & c;
        int cd = ~b & d;
        if (pasoapaso) {
            this.opfuncion[0] = "F(B, C, D) = (B and C) or ((not B) and D)\n";
            this.opfuncion[1] = "    " + intToBinaryString(bc) + " = (B and C)\n";
            this.opfuncion[1] += " OR " + intToBinaryString(cd) + " = ((not B) and D)\n";
        }
        return bc | cd;
        //return (x & y) | (~x & z);
    }

    /**
     * Operación G
     *
     * @param b Palabra de 32 bit de entrada.
     * @param c Palabra de 32 bit de entrada.
     * @param d Palabra de 32 bit de entrada.
     * @return El resultado de la operación.
     */
    private int G(int b, int c, int d) {
        if (pasoapaso) {
            this.opfuncion[0] = "G(B, C, D) = (B xor C xor D)\n";
            this.opfuncion[1] = "    " + intToBinaryString(b) + " = B\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(c) + " = C\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(d) + " = D\n";
        }
        return b ^ c ^ d;
    }

    /**
     * Operación H
     *
     * @param b Palabra de 32 bit de entrada.
     * @param c Palabra de 32 bit de entrada.
     * @param d Palabra de 32 bit de entrada.
     * @return El resultado de la operación.
     */
    private int H(int b, int c, int d) {
        int bc = b & c;
        int bd = b & d;
        int cd = c & d;
        if (pasoapaso) {
            this.opfuncion[0] = "H(B, C, D) = (B and C) or (B and D) or (C and D)\n";
            this.opfuncion[1] = "    " + intToBinaryString(bd) + " = (B and C)\n";
            this.opfuncion[1] += "    " + intToBinaryString(bd) + " = (B and D)\n";
            this.opfuncion[1] += " OR " + intToBinaryString(cd) + " = (C and D))\n";
        }
        return bc | bd | cd;
        //return (b & d) | (c & ~d);
    }

    /**
     * Operación I
     *
     * @param b Palabra de 32 bit de entrada.
     * @param c Palabra de 32 bit de entrada.
     * @param d Palabra de 32 bit de entrada.
     * @return El resultado de la operación.
     */
    private int I(int b, int c, int d) {
        if (pasoapaso) {
            this.opfuncion[0] = "G(B, C, D) = (B xor C xor D)\n";
            this.opfuncion[1] = "    " + intToBinaryString(b) + " = B\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(c) + " = C\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(d) + " = D\n";
        }
        return b ^ c ^ d;
    }

    @Override
    protected void procesaBloque() {
        if (pasoapaso | porbloque) {
            this.track += "====================Procesamiento del bloque " + nBloque() + " ====================\n";
        }
        if (pasoapaso) {
            this.track += "Las palabras del bloque " + nBloque() + " del mensaje son:\n";
            this.track += bloqueToString();
        }

        int a = A;
        int b = B;
        int c = C;
        int d = D;
        int e = E;
        for (int index = 0; index < bloque.length; index++) {
            W[index] = bloque[index];
        }

        if (pasoapaso | porbloque) {
            this.track += "Valores iniciales (A, B, C, D, E)\n";
            this.track += String.format("         t = %2d ", 0) + this.bufferesToStringH(new int[]{a, b, c, d, e}) + "\n";
            this.track += "Primera vuelta:\n";
        }

        int brot = 0;
        int aproc = 0;
        /* Vuelta 1 */
        for (int t = 0; t < 20; t++) {
            brot = this.RL(b, (byte) 30);
            aproc = vuelta("F", a, b, brot, c, d, e, K1, t);
            b = a;
            a = aproc;
            e = d;
            d = c;
            c = brot;
        }

        /* Vuelta 2 */
        if (pasoapaso | porbloque) {
            this.track += "Segunda vuelta:\n";
        }

        for (int t = 20; t < 40; t++) {
            brot = this.RL(b, (byte) 30);
            aproc = vuelta("G", a, b, brot, c, d, e, K2, t);
            b = a;
            a = aproc;
            e = d;
            d = c;
            c = brot;
        }

        /* Vuelta 3 */
        if (pasoapaso | porbloque) {
            this.track += "Tercera vuelta:\n";
        }

        for (int t = 40; t < 60; t++) {
            brot = this.RL(b, (byte) 30);
            aproc = vuelta("H", a, b, brot, c, d, e, K3, t);
            b = a;
            a = aproc;
            e = d;
            d = c;
            c = brot;
        }

        /* Vuelta 4 */
        if (pasoapaso | porbloque) {
            this.track += "Cuarta vuelta:\n";
        }

        for (int t = 60; t < 80; t++) {
            brot = this.RL(b, (byte) 30);
            aproc = vuelta("I", a, b, brot, c, d, e, K4, t);
            b = a;
            a = aproc;
            e = d;
            d = c;
            c = brot;
        }

        if (pasoapaso | porbloque) {
            this.track += "\n";
            this.track += "Actualizaci\u00f3n: (valores iniciales + valores del paso 80)\n";
            this.track += "                " + bufferesToStringH(new int[]{A, B, C, D, E});
            this.track += "              + " + bufferesToStringH(new int[]{a, b, c, d, e});
            this.track += "              ----------------------------------------------\n";
        }
        A = SUM(A, a);
        B = SUM(B, b);
        C = SUM(C, c);
        D = SUM(D, d);
        E = SUM(E, e);
        if (pasoapaso | porbloque) {
            this.track += "                " + bufferesToStringH(new int[]{A, B, C, D, E});
            this.track += "\n";
        }
    }

    /**
     * Representa en texto las operaciones de una vuelta.
     *
     * @param funcion Nombre de la función.
     * @param a Buffer A.
     * @param b Buffer B.
     * @param c Buffer C.
     * @param d Buffer D.
     * @param f Valor resultado de la función.
     * @param K Constante.
     * @param t Número de paso y posición de la palabra a usar en este paso.
     * @param aux1 Suma de E + F(B, C, D) + (A << 5) + W + K @par am arot A
     * rotada a la izquierda. @param brot B rotada a la izquierda. @para m total
     * Suma de todo. @return Operaciones realizadas en for m ato de texto.
     */
    private String operacionToString(String funcion, int a, int b, int c, int d, int e, int f, int K, int t, int aux1, int arot, int brot, int total) {
        String operaciones = "";
        operaciones += this.opfuncion[0];
        operaciones += "A = E + " + funcion + "(B, C, D) + (A <<< 5) + M[i] + K[j]\n";
        operaciones += "E = D; D = C; C = (B <<< 30); B = A\n";
        operaciones += "\n";
        operaciones += bufferesToStringV(new int[]{a, b, c, d, e}, new char[]{'A', 'B', 'C', 'D', 'E'});
        operaciones += "\n";
        if (t >= bloque.length) {
            operaciones += "Generación de la palabra " + (t + 1) + " a partir de cuatro anteriores:\n";
            int pos = t + 1;
            operaciones += String.format("(W[%d - 3] xor W[%d - 8] xor W[%d - 14] xor W[%d - 16]) << 1\n", pos, pos, pos, pos);
            operaciones += "    " + intToBinaryString(W[t-3]) + " = Palabra " + (t-2) + "\n";
            operaciones += "XOR " + intToBinaryString(W[t-8]) + " = Palabra " + (t-7) + "\n";
            operaciones += "XOR " + intToBinaryString(W[t-14]) + " = Palabra " + (t-13) + "\n";
            operaciones += "XOR " + intToBinaryString(W[t-16]) + " = Palabra " + (t-15) + "\n";
            operaciones += "------------------------------------\n";
            operaciones += "    " + intToBinaryString(RR(W[t],(byte)1)) + " Hay que rotar 1 bit a la izquierda\n";
            operaciones += "------------------------------------\n";
            operaciones += "    " + intToBinaryString(W[t]) + " Palabra generada para este paso\n";
            operaciones += "\n";
        }
        operaciones += this.opfuncion[1];
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(f) + " = " + funcion + "(B, C, D)\n";
        operaciones += "  + " + intToBinaryString(e) + " = Valor de E\n";
        operaciones += "  + " + intToBinaryString(arot) + " = A rotado a la izq. de 5 bits\n";
        operaciones += "  + " + intToBinaryString(W[t]) + " = Palabra " + (t + 1) + " del bloque " + nBloque() + "\n";
        operaciones += "  + " + intToBinaryString(K) + " = Constante " + intToHexString(K) + "\n";
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(total) + "\n";
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(total) + " Es ahora la palabra A\n";
        operaciones += "\n";
        operaciones += "La palabra D pasa a ocupar el lugar de E\n";
        operaciones += "La palabra C pasa a ocupar el lugar de D\n";
        operaciones += "La palabra B rotada 30 posiciones a la izquierda:\n";
        operaciones += "    " + intToBinaryString(brot) + " Es ahora la palabra C\n";
        operaciones += "La palabra A pasa a ocupar el lugar de B\n";
        operaciones += "\n";
        operaciones += String.format("Salida:  t = %2d ", t + 1) + bufferesToStringH(new int[]{total, a, brot, c, d});
        operaciones += "---------------------------------------------------------------\n";
        operaciones += "\n";
        return operaciones;
    }

    /**
     * Operaciones realizadas en cada vuelta.
     *
     * @param a Palabra de 32 bit de entrada, operando A.
     * @param b Palabra de 32 bit de entrada, operando B.
     * @param c Palabra de 32 bit de entrada, operando C.
     * @param d Palabra de 32 bit de entrada, operando D.
     * @param e Palabra de 32 bit de entrada, operando E.
     * @param K Palabra de 32 bit de entrada, valor constante almacenado en una
     * matriz.
     * @param t Número de paso y posición de la palabra a utilizar.
     * @return resultado de las operaciones.
     */
    private int vuelta(String funcion, int a, int b, int brot, int c, int d, int e, int K, int t) {
        // Para los pasos posteriores al 16 se genera una palabra
        if (t >= bloque.length) {
            generaPalabra(t);
        }
        int f = 0;
        switch (funcion) {
            case "F":
                f = F(b, c, d);
                break;
            case "G":
                f = G(b, c, d);
                break;
            case "H":
                f = H(b, c, d);
                break;
            case "I":
                f = I(b, c, d);
                break;
        }
        int aux1 = SUM(e, f);
        int arot = RL(a, (byte) 5);
        aux1 = SUM(aux1, arot);
        aux1 = SUM(aux1, W[t]);
        int total = SUM(aux1, K);

        if (pasoapaso) {
            this.track += operacionToString(funcion, a, b, c, d, e, f, K, t, aux1, arot, brot, total);
        }
        if (porbloque) {
            this.track += String.format("         t = %2d ", t + 1) + bufferesToStringH(new int[]{total, a, brot, c, d});
        }
        return total;
    }

    /**
     * Genera una palabra a partir de 4 anteriores.
     *
     * @param pos Posición que ocupará la palabra generada.
     * @return Palabra generada.
     */
    private int generaPalabra(int pos) {
        W[pos] = RL((W[pos - 3] ^ W[pos - 8] ^ W[pos - 14] ^ W[pos - 16]), (byte) 1);
        return W[pos];
    }

    @Override
    public void engineUpdate(byte input) {
        addByteBig(input);
        this.size += 8;
    }

    @Override
    public byte[] engineDigest() {
        if (resumen != null){
            return resumen;
        }
        this.addRellenoBig();
        int[] res = new int[]{A, B, C, D, E};
        if (pasoapaso | porbloque) {
            this.track += "======== Valor hash o resumen final del \u00faltimo bloque ========\n";
            this.track += "Luego el resumen final ser\u00e1:\n";
            this.track += "                " + resumenToString(res);
        }
        resumen = intToBytes(res);
        return resumen;
    }

    @Override
    public void engineReset() {
        A = Ainicial;
        B = Binicial;
        C = Cinicial;
        D = Dinicial;
        E = Einicial;
        if (pasoapaso || porbloque) {
            this.track += "Valor inicial: " + this.bufferesToStringH(new int[]{A, B, C, D, E}) + "\n";
        }
        super.engineReset();
    }
}

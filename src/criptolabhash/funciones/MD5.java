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
public class MD5 extends Hash32 implements Seguimiento{

    private static final int Ainicial = 0x01234567;
    private static final int Binicial = 0x89abcdef;
    private static final int Cinicial = 0xfedcba98;
    private static final int Dinicial = 0x76543210;
    private static final byte S11 = 7;
    private static final byte S12 = 12;
    private static final byte S13 = 17;
    private static final byte S14 = 22;
    private static final byte S21 = 5;
    private static final byte S22 = 9;
    private static final byte S23 = 14;
    private static final byte S24 = 20;
    private static final byte S31 = 4;
    private static final byte S32 = 11;
    private static final byte S33 = 16;
    private static final byte S34 = 23;
    private static final byte S41 = 6;
    private static final byte S42 = 10;
    private static final byte S43 = 15;
    private static final byte S44 = 21;

    /**
     * Instanciación sin seguimiento.
     */
    public MD5(){
        super("MD5");
    }
    
    /**
     * Instanciación con seguimiento.
     * @param pasoapaso Indica si se quiere obtener seguimiento paso a paso o 
     * por bloques.
     */
    public MD5(boolean pasoapaso){
        super("MD5", pasoapaso);
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
        int bd = b & d;
        int cd = c & ~d;
        if (pasoapaso) {
            this.opfuncion[0] = "G(B, C, D) = (B and D) or (C and (not D))\n";
            this.opfuncion[1] = "    " + intToBinaryString(bd) + " = (B and D)\n";
            this.opfuncion[1] += " OR " + intToBinaryString(cd) + " = (C and (not D))\n";
        }
        return bd | cd;
        //return (b & d) | (c & ~d);
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
        if (pasoapaso) {
            this.opfuncion[0] = "H(B, C, D) = (B xor C xor D)\n";
            this.opfuncion[1] = "    " + intToBinaryString(b) + " = B\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(c) + " = C\n";
            this.opfuncion[1] += "XOR " + intToBinaryString(d) + " = D\n";
        }
        return b ^ c ^ d;
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
        int bd = b | ~d;
        if (pasoapaso) {
            this.opfuncion[0] = "I(B, C, D) = C xor (B or (not D))\n";
            this.opfuncion[1] = "    " + intToBinaryString(bd) + " = (B or (not D))\n";
            this.opfuncion[1] += "xOR " + intToBinaryString(c) + " = C\n";
        }
        return c ^ bd;
        //return c ^ (b | ~d);
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
     * @param x Posición de la palabra en el bloque.
     * @param ac Constante.
     * @param t Número de vuelta.
     * @param aux1 Suma de A + F(B, C, D) + X + AC
     * @param arot Rotación a la izquierda .
     * @param s Números de posiciones rotadas.
     * @param total Suma de todo.
     * @return Operaciones realizadas en formato de texto.
     */
    private String operacionToString(String funcion, int a, int b, int c, int d, int f, int x, int ac, int t, int aux1, int arot, byte s, int total) {
        String operaciones = "";
        operaciones += this.opfuncion[0];
        operaciones += "B = B + ((A + " + funcion + "(B, C, D) + M[i] + K[j]) <<< S)\n";
        operaciones += "A = D; D = C; C = B\n";
        operaciones += "\n";
        operaciones += bufferesToStringV(new int[]{a, b, c, d}, new char[]{'A', 'B', 'C', 'D'});
        operaciones += "\n";
        operaciones += this.opfuncion[1];
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(f) + " = " + funcion + "(B, C, D)\n";
        operaciones += "  + " + intToBinaryString(a) + " = Valor de A\n";
        operaciones += "  + " + intToBinaryString(bloque[x]) + " = Palabra " + x + " del bloque " + nBloque() + "\n";
        operaciones += "  + " + intToBinaryString(ac) + " = Valor de constante N\u00ba " + (t - 1) + "\n";
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(aux1) + "\n";
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(arot) + " Rotado a la izquierda de " + s + " bits\n";
        operaciones += "  + " + intToBinaryString(b) + " = Valor de B\n";
        operaciones += "------------------------------------\n";
        operaciones += "    " + intToBinaryString(total) + " = Es ahora la palabra B\n";
        operaciones += "\n";
        operaciones += "La palabra D pasa a ocupar el lugar de A\n";
        operaciones += "La palabra C pasa a ocupar el lugar de D\n";
        operaciones += "La palabra B pasa a ocupar el lugar de C\n";
        operaciones += "\n";
        operaciones += String.format("Salida:  t = %2d ", t) + bufferesToStringH(new int[]{d, total, b, c});
        operaciones += "---------------------------------------------------------------\n";
        return operaciones;
    }

    /**
     * Operaciones realizadas en cada vuelta.
     *
     * @param a Palabra de 32 bit de entrada, operando A.
     * @param b Palabra de 32 bit de entrada, operando B.
     * @param c Palabra de 32 bit de entrada, operando C.
     * @param d Palabra de 32 bit de entrada, operando D.
     * @param x Posición de la palabra del bloque a procesar.
     * @param s Palabra de 32 bit de entrada, desplazamiento a la izquierda.
     * @param ac Palabra de 32 bit de entrada, valor constante almacenado en una
     * matriz.
     * @return resultado de las operaciones.
     */
    private int vuelta(String funcion, int a, int b, int c, int d, int x, byte s, int ac, int t) {
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
        int aux1 = SUM(a, f);
        aux1 = SUM(aux1, bloque[x]);
        aux1 = SUM(aux1, ac);
        int arot = RL(aux1, s);
        int total = SUM(arot, b);
        if (pasoapaso) {
            this.track += operacionToString(funcion, a, b, c, d, f, x, ac, t, aux1, arot, s, total);
        }
        if (porbloque) {
            this.track += String.format("         t = %2d ", t) + bufferesToStringH(new int[]{d, total, b, c});
        }
        return total;
    }

    
    @Override
    protected void procesaBloque() {
        if (pasoapaso | porbloque) {
            this.track += "====================Procesamiento del bloque " + nBloque() + " ====================\n";
        }
        if (pasoapaso) {
            this.track += "Palabras del bloque con los bytes menos significativos a la derecha:\n";
            this.track += bloqueToString();
        }
        int a = A;
        int b = B;
        int c = C;
        int d = D;
        int t = 0;
        if (pasoapaso | porbloque) {
            this.track += "Valores iniciales (A, B, C, D)\n";
            this.track += String.format("         t = %2d ", t) + this.bufferesToStringH(new int[]{a, b, c, d}) + "\n";
            this.track += "Primera vuelta:\n";
        }
        /* Vuelta 1 */
        a = vuelta("F", a, b, c, d, 0, S11, -680876936, ++t); // 1
        d = vuelta("F", d, a, b, c, 1, S12, -389564586, ++t); // 2
        c = vuelta("F", c, d, a, b, 2, S13, 606105819, ++t); // 3
        b = vuelta("F", b, c, d, a, 3, S14, -1044525330, ++t); // 4
        a = vuelta("F", a, b, c, d, 4, S11, -176418897, ++t); // 5
        d = vuelta("F", d, a, b, c, 5, S12, 1200080426, ++t); // 6
        c = vuelta("F", c, d, a, b, 6, S13, -1473231341, ++t); // 7
        b = vuelta("F", b, c, d, a, 7, S14, -45705983, ++t); // 8
        a = vuelta("F", a, b, c, d, 8, S11, 1770035416, ++t); // 9
        d = vuelta("F", d, a, b, c, 9, S12, -1958414417, ++t); //10
        c = vuelta("F", c, d, a, b, 10, S13, -42063, ++t); //11
        b = vuelta("F", b, c, d, a, 11, S14, -1990404162, ++t); //12
        a = vuelta("F", a, b, c, d, 12, S11, 1804603682, ++t); //13
        d = vuelta("F", d, a, b, c, 13, S12, -40341101, ++t); //14
        c = vuelta("F", c, d, a, b, 14, S13, -1502002290, ++t); //15
        b = vuelta("F", b, c, d, a, 15, S14, 1236535329, ++t); //16
        if (pasoapaso | porbloque) {
            this.track += "Segunda vuelta:\n";
        }
        /* Vuelta 2 */
        a = vuelta("G", a, b, c, d, 1, S21, -165796510, ++t); //17
        d = vuelta("G", d, a, b, c, 6, S22, -1069501632, ++t); //18
        c = vuelta("G", c, d, a, b, 11, S23, 643717713, ++t); //19
        b = vuelta("G", b, c, d, a, 0, S24, -373897302, ++t); //20
        a = vuelta("G", a, b, c, d, 5, S21, -701558691, ++t); //21
        d = vuelta("G", d, a, b, c, 10, S22, 38016083, ++t); //22
        c = vuelta("G", c, d, a, b, 15, S23, -660478335, ++t); //23
        b = vuelta("G", b, c, d, a, 4, S24, -405537848, ++t); //24
        a = vuelta("G", a, b, c, d, 9, S21, 568446438, ++t); //25
        d = vuelta("G", d, a, b, c, 14, S22, -1019803690, ++t); //26
        c = vuelta("G", c, d, a, b, 3, S23, -187363961, ++t); //27
        b = vuelta("G", b, c, d, a, 8, S24, 1163531501, ++t); //28
        a = vuelta("G", a, b, c, d, 13, S21, -1444681467, ++t); //29
        d = vuelta("G", d, a, b, c, 2, S22, -51403784, ++t); //30
        c = vuelta("G", c, d, a, b, 7, S23, 1735328473, ++t); //31
        b = vuelta("G", b, c, d, a, 12, S24, -1926607734, ++t); //32
        if (pasoapaso | porbloque) {
            this.track += "Tercera vuelta:\n";
        }
        /* Vuelta 3 */
        a = vuelta("H", a, b, c, d, 5, S31, -378558, ++t); //33
        d = vuelta("H", d, a, b, c, 8, S32, -2022574463, ++t); //34
        c = vuelta("H", c, d, a, b, 11, S33, 1839030562, ++t); //35
        b = vuelta("H", b, c, d, a, 14, S34, -35309556, ++t); //36
        a = vuelta("H", a, b, c, d, 1, S31, -1530992060, ++t); //37
        d = vuelta("H", d, a, b, c, 4, S32, 1272893353, ++t); //38
        c = vuelta("H", c, d, a, b, 7, S33, -155497632, ++t); //39
        b = vuelta("H", b, c, d, a, 10, S34, -1094730640, ++t); //40
        a = vuelta("H", a, b, c, d, 13, S31, 681279174, ++t); //41
        d = vuelta("H", d, a, b, c, 0, S32, -358537222, ++t); //42
        c = vuelta("H", c, d, a, b, 3, S33, -722521979, ++t); //43
        b = vuelta("H", b, c, d, a, 6, S34, 76029189, ++t); //44
        a = vuelta("H", a, b, c, d, 9, S31, -640364487, ++t); //45
        d = vuelta("H", d, a, b, c, 12, S32, -421815835, ++t); //46
        c = vuelta("H", c, d, a, b, 15, S33, 530742520, ++t); //47
        b = vuelta("H", b, c, d, a, 2, S34, -995338651, ++t); //48
        if (pasoapaso | porbloque) {
            this.track += "Cuarta vuelta:\n";
        }
        /* Vuelta 4 */
        a = vuelta("I", a, b, c, d, 0, S41, -198630844, ++t); //49
        d = vuelta("I", d, a, b, c, 7, S42, 1126891415, ++t); //50
        c = vuelta("I", c, d, a, b, 14, S43, -1416354905, ++t); //51
        b = vuelta("I", b, c, d, a, 5, S44, -57434055, ++t); //52
        a = vuelta("I", a, b, c, d, 12, S41, 1700485571, ++t); //53
        d = vuelta("I", d, a, b, c, 3, S42, -1894986606, ++t); //54
        c = vuelta("I", c, d, a, b, 10, S43, -1051523, ++t); //55
        b = vuelta("I", b, c, d, a, 1, S44, -2054922799, ++t); //56
        a = vuelta("I", a, b, c, d, 8, S41, 1873313359, ++t); //57
        d = vuelta("I", d, a, b, c, 15, S42, -30611744, ++t); //58
        c = vuelta("I", c, d, a, b, 6, S43, -1560198380, ++t); //59
        b = vuelta("I", b, c, d, a, 13, S44, 1309151649, ++t); //60
        a = vuelta("I", a, b, c, d, 4, S41, -145523070, ++t); //61
        d = vuelta("I", d, a, b, c, 11, S42, -1120210379, ++t); //62
        c = vuelta("I", c, d, a, b, 2, S43, 718787259, ++t); //63
        b = vuelta("I", b, c, d, a, 9, S44, -343485551, ++t); //64
        if (pasoapaso | porbloque) {
            this.track += "\n";
            this.track += "Actualizaci\u00f3n: (valores iniciales + valores del paso 64)\n";
            this.track += "                " + bufferesToStringH(new int[]{A, B, C, D});
            this.track += "              + " + bufferesToStringH(new int[]{a, b, c, d});
            this.track += "              -------------------------------------\n";
        }
        A = SUM(A, a);
        B = SUM(B, b);
        C = SUM(C, c);
        D = SUM(D, d);
        if (pasoapaso | porbloque) {
            this.track += "                " + bufferesToStringH(new int[]{A, B, C, D});
            this.track += "\n";
        }
    }

    @Override
    public void engineUpdate(byte input) {
        addByteLittle(input);
        this.size += 8;
    }

    @Override
    public byte[] engineDigest() {
        if (resumen != null){
            return resumen;
        }
        this.addRellenoLittle();
        if (pasoapaso | porbloque) {
            this.track += "======== Valor hash o resumen final del \u00faltimo bloque ========\n";
            this.track += "  Deshaciendo la inversi\u00f3n inicial, es decir, representando\n";
            this.track += "los bytes menos significativos de cada palabra a la izquierda\n";
            this.track += "se obtiene:\n";
            this.track += "                " + bufferesToStringH(new int[]{A, B, C, D});
            this.track += "\n";
            this.track += "Luego el resumen final ser\u00e1:\n";
        }
        A = switchEndian(A);
        B = switchEndian(B);
        C = switchEndian(C);
        D = switchEndian(D);
        
        int[] res = new int[]{A, B, C, D};
        if (pasoapaso | porbloque) {
            this.track += "                " + resumenToString(res);
        }
        resumen = intToBytes(res);
        return resumen;
    }

    @Override
    public void engineReset() {
        A = switchEndian(Ainicial);
        B = switchEndian(Binicial);
        C = switchEndian(Cinicial);
        D = switchEndian(Dinicial);
        if (pasoapaso || porbloque) {
            this.track = "Valor inicial: " + this.bufferesToStringH(new int[]{Ainicial, Binicial, Cinicial, Dinicial}) + "\n";
            this.track += "Donde los bytes menos significativos de cada palabra (A, B, C, D)\n";
            this.track += "est\u00e1n a la izquierda. Si los representamos en el orden natural\n";
            this.track += "(bytes menos significativos a la derecha), se obtiene:\n";
            this.track += "\n";
            this.track += "Valor inicial: " + this.bufferesToStringH(new int[]{A, B, C, D}) + "\n";
        }
        super.engineReset();
    }    
}

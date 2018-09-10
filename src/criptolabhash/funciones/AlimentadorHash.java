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

import criptolabhash.control.APISeguimientoHash;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.xml.bind.DatatypeConverter;

/**
 * Esta clase se ocupa de preparar las fuentes de entrada de mensajes para que
 * puedan ser digeridas por las funciones hash.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class AlimentadorHash implements APISeguimientoHash {
    
    @Override
    public String procesaFile(String algoritmo, Boolean nivel, File fichero, StringBuilder informe) throws IOException {
        Hash32 algoritmohash = conseguirInstancia(algoritmo, nivel);
        byte[] hash;
        try (FileChannel canal = FileChannel.open(fichero.toPath())) {
            byte[] bloque = new byte[64];
            ByteBuffer bufer = ByteBuffer.wrap(bloque);
            int longitud;
            while ((longitud = canal.read(bufer)) > 0) {
                bufer.flip();
                algoritmohash.engineUpdate(bloque, 0, longitud);
            }
        }
        hash = algoritmohash.engineDigest();
        informe.append(algoritmohash.getSeguimiento());
        return DatatypeConverter.printHexBinary(hash);
    }

    @Override
    public String procesaText(String algoritmo, Boolean nivel, String texto, StringBuilder informe) {
        Hash32 algoritmohash = conseguirInstancia(algoritmo, nivel);
        byte[] mensaje = texto.getBytes();
        algoritmohash.engineUpdate(mensaje, 0, mensaje.length);
        byte[] hash = algoritmohash.engineDigest();
        informe.append(algoritmohash.getSeguimiento());
        return DatatypeConverter.printHexBinary(hash);
    }

    @Override
    public String procesaHex(String algoritmo, Boolean nivel, String hexadecimal, StringBuilder informe) throws IllegalArgumentException {
        Hash32 algoritmohash = conseguirInstancia(algoritmo, nivel);
        byte[] mensaje = DatatypeConverter.parseHexBinary(hexadecimal);
        algoritmohash.engineUpdate(mensaje, 0, mensaje.length);
        byte[] hash = algoritmohash.engineDigest();
        informe.append(algoritmohash.getSeguimiento());
        return DatatypeConverter.printHexBinary(hash);
    }

    /**
     * Consigue una instancia de algoritmo hash de las características elegidas.
     *
     * @param algoritmo Nombre del algoritmo.
     * @param nivel Detalle del informe.
     * @return Instancia del algoritmo hash.
     */
    private Hash32 conseguirInstancia(String algoritmo, Boolean nivel) {
        if (nivel == null) {
            return FabricaHash.getInstance(algoritmo);
        } else if (nivel) {
            return FabricaHash.getInstancePasoapaso(algoritmo);
        } else {
            return FabricaHash.getInstancePorbloque(algoritmo);
        }
    }
}

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

import javax.xml.bind.DatatypeConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class MD5Test {
    
    public MD5Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test del cálculo de hash MD5.
     */
    @Test
    public void testEngineDigest() {
        System.out.println("engineDigest");
        byte[] mensaje = "Hola".getBytes();
        Hash32 instance = new MD5();
        for(byte caracter: mensaje){
            instance.engineUpdate(caracter);
        }
        byte[] hash = instance.engineDigest();
        byte[] hashs = DatatypeConverter.parseHexBinary("F688AE26E9CFA3BA6235477831D5122E");
        assertArrayEquals(hashs, hash);
    }
    
     /**
     * Test del cálculo de hash MD5 con más de 64 caracteres.
     */
    @Test
    public void testEngineDigestPlus() {
        System.out.println("engineDigestPlus");
        byte[] mensaje = "Probando un texto que supere los sesenta y cuatro caracteres escritos".getBytes();
        Hash32 instance = new MD5();
        for(byte caracter: mensaje){
            instance.engineUpdate(caracter);
        }
        byte[] hash = instance.engineDigest();
        byte[] hashs = DatatypeConverter.parseHexBinary("c54955c98e3e757bd84f776ddb323f19");
        assertArrayEquals(hashs, hash);
    }
    
    /**
     * Test para probar el seguimiento paso a paso.
     */
    @Test
    public void testGetSeguimientoPasoaPaso(){
        System.out.println("getSeguimientoPasoaPaso");
        byte[] mensaje = "Hola".getBytes();
        Hash32 instance = new MD5(true);
        for(byte caracter: mensaje){
            instance.engineUpdate(caracter);
        }
        System.out.println(instance.getSeguimiento());
        byte[] hash = instance.engineDigest();
        byte[] hashs = DatatypeConverter.parseHexBinary("F688AE26E9CFA3BA6235477831D5122E");
        assertArrayEquals(hashs, hash);
    }
    
    /**
     * Test para probar el seguimiento paso a paso.
     */
    @Test
    public void testGetSeguimientoPorBloque(){
        System.out.println("getSeguimientoPorBloque");
        byte[] mensaje = "Hola".getBytes();
        Hash32 instance = new MD5(false);
        for(byte caracter: mensaje){
            instance.engineUpdate(caracter);
        }
        System.out.println(instance.getSeguimiento());
        byte[] hash = instance.engineDigest();
        byte[] hashs = DatatypeConverter.parseHexBinary("F688AE26E9CFA3BA6235477831D5122E");
        assertArrayEquals(hashs, hash);
    }
}

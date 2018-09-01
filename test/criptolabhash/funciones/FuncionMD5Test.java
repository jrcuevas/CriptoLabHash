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
public class FuncionMD5Test {
    private static MD5 instance;
    
    public FuncionMD5Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        instance = new MD5(true);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        instance.engineReset();
    }

    /**
     * Test of engineDigest method, of class Funcion32.
     */
    @Test
    public void testEngineDigest() {
        System.out.println("engineDigest");
        byte[] mensaje = "Hola".getBytes();
        for(byte caracter: mensaje){
            instance.engineUpdate(caracter);
        }
        byte[] hash = instance.engineDigest();
        byte[] hashs = DatatypeConverter.parseHexBinary("F688AE26E9CFA3BA6235477831D5122E");
        System.out.println(instance.getSeguimiento());
        assertArrayEquals(hashs, hash);
    }
    
}

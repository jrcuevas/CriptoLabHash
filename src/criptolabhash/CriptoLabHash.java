/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criptolabhash;

import criptolabhash.ataques.servidor.ServidorCumpleanos;
import criptolabhash.vista.CriptoLab;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author jrcuevas
 */
public class CriptoLabHash {

    /**
     * Dirección ip donde quiere que escuche el servidor.
     */
    private static InetAddress host;

    /**
     * Puerto de escucha del servidor.
     */
    private static int port;

    /**
     * @param args the command line arguments
     * @throws InterruptedException Si se ha recibido una orden de parar ejecución.
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length > 2) {
            imprimeAyuda();
        }
        try {
            switch (args.length) {
                case 0:
                    host = InetAddress.getLocalHost();
                    port = 10001;
                    break;
                case 1:
                    host = InetAddress.getLocalHost();
                    port = Integer.parseInt(args[0]);
                    break;
                default:
                    host = InetAddress.getByName(args[0]);
                    port = Integer.parseInt(args[1]);
                    break;
            }
        } catch (NumberFormatException | UnknownHostException ex) {
            System.out.println("Error al arrancar el servidor: " + ex.getMessage());
            imprimeAyuda();
        }

        try {
            ServidorCumpleanos servidor = new ServidorCumpleanos(port, host);
            servidor.start();
            CriptoLab.conectaAPI(servidor.getAPIServer());
            CriptoLab.main(null); // Arranca la interface gráfica
        } catch (IOException ex) {
            System.out.println("Error al conectar con el cliente: " + ex.getMessage());
        }
    }

    private static void imprimeAyuda() {
        StringBuilder ayuda = new StringBuilder();
        ayuda.append("Copyright (C) 2018 José Ramón Cuevas\n"
                + "\n"
                + "This program is free software: you can redistribute it and/or modify\n"
                + "it under the terms of the GNU General Public License as published by\n"
                + "the Free Software Foundation, either version 3 of the License, or\n"
                + "(at your option) any later version.\n"
                + "\n"
                + "This program is distributed in the hope that it will be useful,\n"
                + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
                + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
                + "GNU General Public License for more details.\n"
                + "\n"
                + "You should have received a copy of the GNU General Public License\n"
                + "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n");

        ayuda.append("\nDESCRIPCION\n"
                + "Este módulo es parte de la herramienta educativa sobre funciones hash \n"
                + "llamada CriptoLabHash. Este es el módulo servidor, que se ocupa de \n"
                + "recibir los códigos generados por los clientes, y guardar las colisiones \n"
                + "encontradas.\n\n");

        ayuda.append("\nUSO\n"
                + "Para ejecutar el aervidor se puede realizar sin parámetros, con lo que \n"
                + "la ip de escucha serán todas las del equipo y el puerto será el 10001.\n"
                + "Tambien se puede realizar con un parámetro, con lo que la ip de escucha \n"
                + "serán todas las del equipo y el puerto el seleccionado. Y por último se \n"
                + "puede facilitar la ip y el puerto donde se quiera que escuche.\n\n"
                + "Como ejemplos:\n"
                + "java -jar CriptoLabHash.jar 127.0.0.1 10001\n"
                + "java -jar CriptoLabHash.jar 10001"
                + "java -jar CriptoLabHash.jar");

        System.out.print(ayuda.toString());
        System.exit(1);
    }
}

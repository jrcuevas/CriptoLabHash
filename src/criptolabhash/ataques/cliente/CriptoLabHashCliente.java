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
package criptolabhash.ataques.cliente;

import criptolabhash.control.APIClienteHash;
import java.io.IOException;

/**
 * Clase principal de arranque de la aplicación cliente de ataques a los
 * algoritmos hash.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class CriptoLabHashCliente {

    /**
     * Dirección ip del equipo en el que se encuentra el servidor.
     */
    private static String host;

    /**
     * Puerto de escucha del servidor.
     */
    private static int port;

    /**
     * Arranque delcliente.
     *
     * @param args IP y puerto de escucha del servidor.
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            imprimeAyuda();
        }
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            imprimeAyuda();
        }

        DataBaseHashCliente dbcliente;
        APIClienteHash apicliente = null;
        try {
            dbcliente = new DataBaseHashCliente(host, port);
            dbcliente.start();
            apicliente = dbcliente;
            monitorizaEstado(apicliente, 0);
        } catch (IOException ex) {
            System.out.println("Error al conectar con el servidor: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Apagado del cliente.");
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
                + "llamada CriptoLabHash. Este es un módulo de generación de códigos hash \n"
                + "que puede ser ejecutado en cualquier máquina que disponga de JVM 1.8 o \n"
                + "superior. Para su ejecución se necesita tener arrancada la aplicación \n"
                + "de servidor CriptoLabHash en un equipo accesible desde este, y saber \n"
                + "cual es su ip y puerto de escucha.\n");

        ayuda.append("\nUSO\n"
                + "Para ejecutar el módulo cliente en la misma máquina que el servidor y\n"
                + "que está escuchando por el puerto 10001.\n\n"
                + "java -jar CriptoLabHash.jar 127.0.0.1 10001\n\n");

        System.out.print(ayuda.toString());
        System.exit(1);
    }

    /**
     * Muestra el estado de calculos del cliente cada segundo.
     *
     * @param cliente Cliente.
     * @param segundos tiempo de monitorización.
     * @throws InterruptedException Si se ha recibido una orden de parar la
     * ejecución.
     */
    public static void monitorizaEstado(APIClienteHash cliente, int segundos) throws InterruptedException {
        int timeout = segundos;
        long ant = 0;
        DataBaseHashCliente db = (DataBaseHashCliente) cliente;
        while (timeout > 0 || segundos == 0) {
            Thread.sleep(1000);
            if (!db.isAlive()) {
                break;
            }
            long act = cliente.numeroHashes();
            long hs = act - ant;
            hs = (hs < 0) ? 0 : hs;
            System.out.println("Nº Hashes: " + cliente.numeroHashes() + "  hashes/s " + hs + "  Nº colisiones: " + cliente.numeroColisiones() + "  Nº pseudocolisiones: " + cliente.numeroPseudocolisiones());
            ant = act;
            if (segundos != 0) {
                timeout--;
            }
        }
    }
}

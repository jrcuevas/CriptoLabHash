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
package criptolabhash.ataques.servidor;

import criptolabhash.control.APIServerHash;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que se encarga de ir atendiendo las peticiones de conexión.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class ServidorCumpleanos extends Thread {

    private final DataBaseHashServer dbhashserver;
    private final int puerto;
    private boolean activo;

    /**
     * Implementación del servidor con MD5 como algoritmo por defecto y mensajes
     * del tamaño de 10 bytes.
     *
     * @param puerto Puerto donde estará esperando a los clientes.
     */
    public ServidorCumpleanos(int puerto) {
        this.activo = true;
        this.puerto = puerto;
        dbhashserver = new DataBaseHashServer("MD5", 10);
    }

    @Override
    public void run() {

        try {
            ServerSocket serversocket = new ServerSocket(puerto);

            while (activo) {
                Socket cliente;
                try {
                    System.out.println("Esperando cliente.");
                    cliente = serversocket.accept();
                    Thread hilo = new HiloServidorCliente(cliente, this.dbhashserver);
                    hilo.start();
                    System.out.println("Cliente arranacado: " + hilo.getId());
                } catch (IOException ex) {
                    System.out.println("No se ha podido conectar con el cliente: " + ex.getMessage());
                }

            }
        } catch (IOException ex) {
            System.out.println("No se ha podido arrancar el servidor: " + ex.getMessage());
        }
    }

    /**
     * Devuelve la referencia del servidor para poder interactuar con el.
     *
     * @return Interfaz de comunicación con el servidor.
     */
    public APIServerHash getAPIServer() {
        return this.dbhashserver;
    }
}

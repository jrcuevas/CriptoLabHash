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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que se encarga de ir atendiendo las peticiones de conexión.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class ServidorCumpleanos extends Thread {

    private final DataBaseHashServer dbhashserver;
    private final ServerSocket serversocket;
    public boolean activo;

    /**
     * Implementación del servidor con MD5 como algoritmo por defecto y mensajes
     * del tamaño de 10 bytes.
     *
     * @param puerto Puerto donde estará esperando a los clientes.
     * @param ip Direción io de escucha.
     * @throws IOException Si ha habido algún problema al arrancar el servidor.
     */
    public ServidorCumpleanos(int puerto, InetAddress ip) throws IOException {
        serversocket = new ServerSocket(puerto, 1, ip);
        this.activo = true;
        dbhashserver = new DataBaseHashServer("MD5", 10, infoConexion());
    }

    @Override
    public void run() {
        while (activo) {
            Socket cliente;
            try {
                cliente = serversocket.accept();
                Thread hilo = new HiloServidorCliente(cliente, this.dbhashserver);
                hilo.start();
            } catch (Exception ex) {
                System.out.println("No se ha podido conectar con el cliente: " + ex.getMessage());
                break;
            }
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

    /**
     * Ip y puerto de escucha.
     *
     * @return ip y puertode escucha.
     */
    private String infoConexion() {
        return "Escuchando en: " + serversocket.getInetAddress().getHostAddress() + ":" + serversocket.getLocalPort();
    }
}

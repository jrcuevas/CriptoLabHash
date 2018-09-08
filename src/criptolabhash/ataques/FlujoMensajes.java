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
package criptolabhash.ataques;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Esta clase se encarga del control de flujo de los mensajes enviados entre
 * Cliente y Servidor.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public abstract class FlujoMensajes extends Thread {

    /**
     * Canal de comunicación.
     */
    private final Socket socket;

    /**
     * Stream por el que se envian los mensajes.
     */
    private final ObjectOutputStream salida;

    /**
     * Manejo de los mensajes de entrada.
     */
    protected ColasEntrada colaentrada;

    /**
     * Indica si el canal está activo.
     */
    protected boolean activo;

    public FlujoMensajes(Socket socket) throws IOException {
        this.socket = socket;
        salida = new ObjectOutputStream(this.socket.getOutputStream());
        this.activo = true;
        this.colaentrada = new ColasEntrada(socket);
        this.colaentrada.start();
    }

    @Override
    public final void run() {
        try {
            while (activo) {
                Mensaje comando = colaentrada.leeComando();
                if (comando != null) {
                    procesaMensajesEntrantes(comando);
                }
                if (!colaentrada.isAlive()) {
                    break;
                }
            }
        } catch (InterruptedException ex) {
        }
        liberaRecursos();
    }

    /**
     * Envía respuesta de confirmación.
     *
     * @param mensaje ACK
     */
    protected void enviaACK(Mensaje mensaje) {
        enviaMensaje(new Mensaje(TMensaje.ACK).setSecuencia(mensaje.getSecuencia()));
    }

    /**
     * Envía respuesta de error.
     *
     * @param mensaje NOACK
     */
    protected void enviaNOACK(Mensaje mensaje) {
        enviaMensaje(new Mensaje(TMensaje.NOACK).setSecuencia(mensaje.getSecuencia()));
    }

    /**
     * Procesa los mensajes que no sean respuestas.
     *
     * @param mensaje Mensaje con el comando a procesar.
     */
    protected abstract void procesaMensajesEntrantes(Mensaje mensaje);

    /**
     * Envia un mensaje y si hay un error de conexión elimina el cliente y
     * cierra la conexión.
     *
     * @param mensaje Mensaje a enviar.
     */
    protected final synchronized void enviaMensaje(Mensaje mensaje) {
        try {
            //System.out.println("Envia mensaje " + mensaje.toString() + " id " + mensaje.getSecuencia());
            salida.writeObject(mensaje);
        } catch (IOException ex) {
            this.activo = false;
        }
    }

    public SocketAddress getConexionHost() {
        return this.socket.getRemoteSocketAddress();
    }

    /**
     * Libera recursos del hilo. Puede ser reescrito si se necesita liberar
     * recursos de la subclase.
     */
    protected void liberaRecursos() {
        this.colaentrada.activo = false;
        if (colaentrada.isAlive()) {
            colaentrada.interrupt();
        }
        this.activo = false;
        try {
            if (salida != null) {
                salida.close();
            }
        } catch (IOException ex) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
        }
    }
}

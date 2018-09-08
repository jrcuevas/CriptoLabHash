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
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Clase que gestiona los mensajes de entrada y los reparte en dos colas
 * diferenciadas. Una es para las peticiones de entrada y otra son las
 * respuestas a mensajes enviados anteriormente.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class ColasEntrada extends Thread {

    /**
     * Stream por el que se reciben los mensajes.
     */
    private final ObjectInputStream entrada;

    /**
     * Respuestas pendientes de atender.
     */
    protected final Map<Long, Mensaje> colarespuestas;

    /**
     * Peticiones entrantes.
     */
    protected final Queue<Mensaje> colapeticiones;

    /**
     * Tiempo máximo de espera para recibir respuesta.
     */
    private static final int TIMEOUT = 1000;
    
    /**
     * En ejecución.
     */
    protected volatile boolean activo;

    /**
     * Constructor.
     *
     * @param socket Canal de comunicación.
     * @throws IOException En caso de no poder realizar la comunicación.
     */
    public ColasEntrada(Socket socket) throws IOException {
        entrada = new ObjectInputStream(socket.getInputStream());
        this.colarespuestas = new HashMap();
        this.colapeticiones = new LinkedList();
        this.activo = true;
    }

    @Override
    public void run() {
        try {
            while (activo) {
                Mensaje mensaje = recibeMensaje();
                if (mensaje == null) {
                    break;
                }
                if (mensaje.toString().endsWith("ACK")) {
                    synchronized (colarespuestas) {
                        colarespuestas.put(mensaje.getSecuencia(), mensaje);
                        colarespuestas.notifyAll();
                    }
                } else {
                    synchronized (colapeticiones) {
                        colapeticiones.add(mensaje);
                        colapeticiones.notify();
                    }
                }
            }
        } catch (InterruptedException ex) {
        }
        liberaRecursos();
    }

    /**
     * Comprueba si hay mensajes en la cola. Si no hay mensajes, espera hasta
     * que llegue uno. Si ha pasado TIMEOUT y no ha llegado mensaje, devuelve
     * null.
     *
     * @return Primer comando que llegue, null si no hay comandos.
     * @throws InterruptedException Si se ha pedido una interrupción del hilo.
     */
    public Mensaje leeComando() throws InterruptedException {
        synchronized (colapeticiones) {
            colapeticiones.wait(TIMEOUT);
            return colapeticiones.poll();
        }
    }

    /**
     * Espera hasta que un mensaje con la secuencia adecuada se encuentre en la
     * cola, si se encuentra y no se corresponde con el tipo elimina el cliente
     * y se desconecta de el.
     *
     * @param tipo Tipo de mensaje buscado.
     * @param secuencia Secuencia del mensaje buscado.
     * @return Mensaje esperado. o nulo si se ha sobrepasado el TIMEOUT.
     */
    public final Mensaje esperaRespuesta(Mensaje tipo, long secuencia) {
        Mensaje mensaje;
        synchronized (colarespuestas) {
            long t1 = System.currentTimeMillis();
            try {
                while (!colarespuestas.containsKey(secuencia)) {
                    colarespuestas.wait(TIMEOUT);
                    if ((System.currentTimeMillis() - t1) >= TIMEOUT) {
                        throw new InterruptedException("EsperaRespuesta: Tiempo de espera superado.");
                    }
                }
                mensaje = colarespuestas.remove(secuencia);
                if (mensaje.toString().equals(tipo.toString())) {
                    return mensaje;
                }
            } catch (InterruptedException ex) {
            }
            return null;
        }
    }

    /**
     * Espera a que llege un mensaje.
     *
     * @return El mensaje recogido.
     * @throws InterruptedException Si ha habido algún tipo de error en la
     * conexión.
     */
    private Mensaje recibeMensaje() throws InterruptedException {
        try {
            Mensaje respuesta = (Mensaje) entrada.readObject();
            //System.out.println("Recibe mensaje " + respuesta.toString() + " id " + respuesta.getSecuencia());
            return respuesta;
        } catch (ClassNotFoundException | IOException ex) {
            throw new InterruptedException("Fallo de conexión al recibir: " + ex.getMessage());
        }
    }

    /**
     * Libera recursos antes de salir.
     */
    private void liberaRecursos() {
        this.activo = false;
        try {
            if (entrada != null) {
                entrada.close();
            }
        } catch (IOException ex) {
        }
    }
}

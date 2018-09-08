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

import criptolabhash.ataques.Bytes;
import criptolabhash.ataques.FlujoMensajes;
import criptolabhash.ataques.Mensaje;
import criptolabhash.ataques.TMensaje;
import java.io.IOException;
import java.net.Socket;

/**
 * Este hilo es el que servirá de puente de conexión con un cliente.
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class HiloServidorCliente extends FlujoMensajes implements FuncionesCliente {

    /**
     * Acceso a la base de datos del servidor.
     */
    private final DataBaseHashServer dbhashserver;

    /**
     * Establece una conexión con un cliente.
     *
     * @param socket Canal de comunicación abierto.
     * @param dbhashserver Base de datos del servidor.
     * @throws IOException Si ha habido algún error de conexión.
     */
    public HiloServidorCliente(Socket socket, DataBaseHashServer dbhashserver) throws IOException {
        super(socket);
        this.dbhashserver = dbhashserver;

    }

    @Override
    protected void procesaMensajesEntrantes(Mensaje mensaje) {
        switch (mensaje.toString()) {
            case "REGISTRO":
                dbhashserver.registroCliente(this);
                enviaACK(mensaje);
                if (dbhashserver.isClientesactivos()) {
                    startCliente();
                }
                break;
            case "PUTHASH":
                dbhashserver.addHash(mensaje.getMensaje(), this);
                enviaACK(mensaje);
                break;
            default:
                System.out.println("Mensaje " + mensaje.toString() + " no definido.");
        }
    }

    @Override
    public void startCliente() {
        Mensaje start = new Mensaje(TMensaje.START);
        enviaMensaje(start);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), start.getSecuencia());
    }

    @Override
    public void stopCliente() {
        Mensaje stop = new Mensaje(TMensaje.STOP);
        enviaMensaje(stop);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), stop.getSecuencia());
    }

    @Override
    public void resetCliente() {
        Mensaje reset = new Mensaje(TMensaje.RESET);
        enviaMensaje(reset);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), reset.getSecuencia());
    }

    @Override
    public void apagaCliente() {
        Mensaje reset = new Mensaje(TMensaje.SHUTDOWN);
        enviaMensaje(reset);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), reset.getSecuencia());
    }

    @Override
    public void setFuncionCliente(String algoritmo, int sizemsg, int sizehash) {
        Mensaje seteo = new Mensaje(TMensaje.SETFUNCION).setAlgoritmo(algoritmo, sizemsg, sizehash);
        enviaMensaje(seteo);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), seteo.getSecuencia());
    }

    @Override
    public Bytes getMessageCliente(Bytes hash, boolean ultimo) {
        Mensaje getmensaje = new Mensaje(TMensaje.GETMESSAGE).setMensaje(hash).setAlgoritmo(null, (ultimo) ? 0 : -1, 0);
        enviaMensaje(getmensaje);
        Mensaje respuesta = colaentrada.esperaRespuesta(new Mensaje(TMensaje.GETMESSAGEACK), getmensaje.getSecuencia());
        return (respuesta == null)? null: respuesta.getMensaje();
    }

    @Override
    protected void liberaRecursos() {
        dbhashserver.eliminaCliente(this);
        super.liberaRecursos();
    }
}

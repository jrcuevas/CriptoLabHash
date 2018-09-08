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

import criptolabhash.ataques.Bytes;
import criptolabhash.ataques.FlujoMensajes;
import criptolabhash.ataques.Mensaje;
import criptolabhash.ataques.NoSetException;
import criptolabhash.ataques.TMensaje;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class HiloClienteServidor extends FlujoMensajes implements FuncionesServidor {

    /**
     * Acceso a la base de datos del cliente.
     */
    private final DataBaseHashCliente dbhashcliente;

    /**
     * Establece una conexión con el servidor.
     *
     * @param ip Dirección ip del servidor.
     * @param port Puerto en el que está escuchando el servidor.
     * @param dbhashcliente Base de datos del cliente.
     * @throws IOException Si ha habido algún error de conexión.
     */
    public HiloClienteServidor(String ip, int port, DataBaseHashCliente dbhashcliente) throws IOException {
        super(new Socket(ip, port));
        this.dbhashcliente = dbhashcliente;
    }

    @Override
    protected void procesaMensajesEntrantes(Mensaje mensaje) {
        switch (mensaje.toString()) {
            case "SETFUNCION":
                dbhashcliente.setFuncionCliente(mensaje.getAlgoritmo(), mensaje.getSizeMsg(), mensaje.getSizeHash());
                enviaACK(mensaje);
                break;
            case "START":
                try {
                    dbhashcliente.startCliente();
                    enviaACK(mensaje);
                } catch (NoSetException ex) {
                    enviaNOACK(mensaje);
                }
                break;
            case "STOP":
                dbhashcliente.stopCliente();
                enviaACK(mensaje);
                break;
            case "RESET":
                dbhashcliente.resetCliente();
                enviaACK(mensaje);
                break;
            case "SHUTDOWN":
                dbhashcliente.apagaCliente();
                enviaACK(mensaje);
                break;
            case "GETMESSAGE":
                boolean ultimo = mensaje.getSizeMsg() == 0;
                Bytes respuesta = dbhashcliente.getMessageCliente(mensaje.getMensaje(), ultimo);
                enviaMensaje(new Mensaje(TMensaje.GETMESSAGEACK).setSecuencia(mensaje.getSecuencia()).setMensaje(respuesta));
                break;
            default:
                System.out.println("Mensaje " + mensaje.toString() + " no definido.");
        }
    }

    @Override
    public void registroServidor() {
        Mensaje registro = new Mensaje(TMensaje.REGISTRO);
        enviaMensaje(registro);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), registro.getSecuencia());
    }

    @Override
    public void putHashServidor(Bytes hash) {
        Mensaje puthash = new Mensaje(TMensaje.PUTHASH).setMensaje(hash);
        enviaMensaje(puthash);
        colaentrada.esperaRespuesta(new Mensaje(TMensaje.ACK), puthash.getSecuencia());
    }

    @Override
    protected void liberaRecursos() {
        super.liberaRecursos();
    }
}

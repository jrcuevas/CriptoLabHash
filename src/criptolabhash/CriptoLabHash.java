/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criptolabhash;

import criptolabhash.ataques.servidor.DataBaseHashServer;
import criptolabhash.ataques.servidor.ServidorCumpleanos;
import criptolabhash.control.APIServerHash;

/**
 *
 * @author jrcuevas
 */
public class CriptoLabHash {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        ServidorCumpleanos servidor = new ServidorCumpleanos(11000);
        servidor.start();
        APIServerHash srv = servidor.getAPIServer();
        srv.setAlgoritmo("SHA1", 2, 2);
        srv.arrancaAtaque();
        System.out.println("Algoritmo: " + srv.getHashName() + "  Tamaño mensaje: " + srv.getSizeMsg() + "  Tamaño hash: " + srv.getSizeHash());
        printtime(srv, 20);
        //srv.paraAtaque();
        //Thread.sleep(10000);
        //srv.arrancaAtaque();
        //printtime(srv, 10);
        //srv.resetearSistema();
        //printtime(srv, 10);
        srv.setAlgoritmo("MD5", 2, 2);
        srv.arrancaAtaque();
        printtime(srv, 20);
        srv.desconectaClientes();
        printtime(srv, 10);
    }

    public static void printtime(APIServerHash srv, int segundos) throws InterruptedException {
        int timeout = segundos;
        DataBaseHashServer db = (DataBaseHashServer) srv;
        while (timeout > 0) {
            Thread.sleep(1000);
            System.out.println("Nº clientes: " + srv.nClientesConectados() + "  Nº hashes: " + srv.nHashCalculados() + "  Nº colisiones: " + srv.nColisiones() + "  Nº pseudocolisiones: " + srv.nPseudoColisiones());
            timeout--;
        }
    }

}

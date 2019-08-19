/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import excecoes.SocketTratadoException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teeu Guima
 */
public class ClienteTratamento extends Thread {

    // private OutputStream output;
    //  private InputStream input;
    private Socket socket;
    private ConectionIO cliente;

    public ClienteTratamento(Socket socket, ConectionIO cliente) {
        this.socket = socket;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Entrou!");
                cliente.tratar();
            } catch (IOException | InterruptedException | SocketTratadoException ex) {
                //ex.printStackTrace();
                interrupt();
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import excecoes.SocketTratadoException;
import java.io.IOException;
import java.net.Socket;

/**Classe que herda Thread afim de gerenciar as conexões com servidores/clientes 
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
                cliente.tratar();
            } catch (IOException | InterruptedException | SocketTratadoException ex) {
                //ex.printStackTrace();
                interrupt();
            }
        }
    }

}

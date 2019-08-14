/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import facade.ClienteServidorFacade;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocolo.Mensagem;

/**
 *
 * @author Teeu Guima
 */
public class ClienteTratamento extends Thread {

    private ControllerDeTratamento tratamento;
    private ControladorDeMensagens mensagens;
    // private OutputStream output;
    //  private InputStream input;
    private Socket socket;
    private ConectionIO cliente;

    public ClienteTratamento(Socket socket, ClienteServidorFacade facade) throws IOException {

        this.mensagens = facade.getMensagem();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;
    }

    public ClienteTratamento(ConectionIO cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {

        try {
            //System.out.println("Entrou!");
                cliente.tratar();
        } catch (IOException ex) {
            //ex.printStackTrace();

        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteTratamento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    

    
}

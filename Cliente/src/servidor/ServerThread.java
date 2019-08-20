/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import facade.ClienteServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Classe que estende Thread para poder atender
 * clientes diferentes.
 *
 * @author Mateus Guimarães 
 */
public class ServerThread extends Thread {

    private ServerSocket server;
    private Socket socket;
    private ThreadConections tcIO;
    private ClienteServidorFacade facade;

    public ServerThread(int porta, ClienteServidorFacade facade) throws IOException {
        server = new ServerSocket(porta);
        this.facade = facade;
    }

    @Override
    public void run() {

        while (true) {

            try {
                facade.criandoArquivos();
                facade.lerDados();
                this.socket = server.accept();
                System.out.println("O cliente conectado possui o endereço: " + socket.getInetAddress().getHostAddress());
                this.tcIO = new ThreadConections(new ConectionIO(socket, facade));
                new Thread(tcIO).start();
                //   TratamentoServidor serverThreadThread = new TratamentoServidor(server.accept(),this ,new ConectionIO(server.accept(), facade, this));

                //   serverThread.add(serverThreadThread);
                //  serverThreadThread.start();
            } catch (IOException ex) {
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

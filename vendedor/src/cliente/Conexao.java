/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import controller.ControladorDeMensagens;
import controller.ControllerDeTratamento;
import facade.ClienteServidorFacade;
import servidor.ServerThread;
import java.io.IOException;
import java.net.Socket;
import util.Console;

/**
 * Classe responsável por operar as conexões com servidor.
 *
 * @author Teeu Guima
 */
public class Conexao {

    private final int PORTASERVIDOR = 5665;
    private ClienteServidorFacade facade;

    public Conexao(ClienteServidorFacade facade) {
        this.facade = facade;
    }

    public void criarServidor() throws IOException {
        System.out.println("Qual porta deseja para operar seu servidor?");
        int porta = Console.readInt();
        ServerThread serverTh = new ServerThread(porta);
        serverTh.start();
        System.out.println("Servidor inicializado na porta"+ porta);
    }

    public void conectarComCliente(String ip, int porta) throws IOException {
        Socket socket = new Socket(ip, porta);
        try {
            
            
        ClienteTratamento tratamento = new ClienteTratamento(new ConectionIO(socket, facade));
        new Thread(tratamento).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

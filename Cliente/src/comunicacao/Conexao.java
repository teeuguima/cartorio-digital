/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacao;

import cliente.ClienteTratamento;
import cliente.ConectionIO;
import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import facade.ClienteServidorFacade;
import java.io.FileNotFoundException;
import servidor.ServerThread;
import java.io.IOException;
import java.net.Socket;
import util.Console;

/**Classe tem por função criar um serverSocket para escutar as conexões
 * estabelecidas na porta definida e criar conexões para diferentes
 * servidores, sendo clientes ou cartórios!
 *
 * @author Mateus Guimarães
 */
public class Conexao {

    private ClienteServidorFacade facade;
    private ServerThread serverTh;
    public Conexao(ClienteServidorFacade facade) throws IOException, FileNotFoundException, ClassNotFoundException {
        this.facade = facade;
    }

    public void criarServidor() throws IOException, FileNotFoundException, ClassNotFoundException {
     
        System.out.println("Qual porta deseja para operar seu servidor?");
        int porta = Console.readInt();
        serverTh = new ServerThread(porta,new ClienteServidorFacade(facade.getMensagem()));
        serverTh.start();
        System.out.println("Servidor inicializado na porta [" + porta+"]");
     
    }

    public void conectarComCliente(String ip, int porta) throws IOException {
        Socket socket = new Socket(ip, porta);
        try {
            ClienteTratamento tratamento = new ClienteTratamento(socket,new ConectionIO(socket, facade));
            new Thread(tratamento).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

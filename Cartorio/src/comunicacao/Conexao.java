package comunicacao;

import facade.ServidorFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Console;

/**
 * Classe tem por função criar um serverSocket para escutar as conexões
 * estabelecidas na porta definida!
 *
 * @author Mateus Guimarães
 */
public class Conexao {

    private ServerSocket serverSocket;
    private int porta;
    private ConectionIO io;

    private final int PORTACARTORIO = 5555;

    private ServidorFacade facade;

    public Conexao() {

    }

    public Conexao(ServidorFacade facade) {
        this.facade = facade;
    }
    
    public ServerSocket getSocket() {
        return serverSocket;

    }

    /**
     * Método que solicita uma porta para inicialização do servidor.
     *
     * @throws IOException
     */
    public void criarServerSocket() throws IOException {
        System.out.println("Defina uma porta válida para esse cartório!");
        this.porta = Console.readInt();
        serverSocket = new ServerSocket(porta);
    }

    public int getPorta() {
        return porta;
    }

    /**Método que escuta conexões na porta definida,
     * retornando um socket obtido!
     * 
     * @return Socket
     * @throws IOException 
     */
    public Socket esperandoConexao() throws IOException {
        //Faz o serverSocket esperar uma conexão, só da o retorno quando a conexão é estabelecida
        System.out.println("Esperando a resposta do cliente .....");
        System.out.println("Fique atento se precisar dar permissão ao Firewall do seu Sistema Operacional");
        Socket socket = serverSocket.accept();
        System.out.println("conexão estabelecida com o cliente");
        return socket;
    }

}

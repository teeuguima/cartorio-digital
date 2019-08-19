package comunicacao;

import facade.ServidorFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Console;

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

    public void iniciar() throws IOException {
        criarServerSocket();
        Socket socket = esperandoConexao();
        if (socket.isConnected()) {
            io = new ConectionIO(socket, facade);
        }   
    }

    public ConectionIO getConectionIO() {
        return io;
    }

    public ServerSocket getSocket() {
        return serverSocket;

    }

    public void criarServerSocket() throws IOException {
        //this.porta = porta;
        System.out.println("Defina uma porta válida para esse cartório!");
        this.porta = Console.readInt();
        serverSocket = new ServerSocket(porta);
    }

    public int getPorta() {
        return porta;
    }

    public Socket esperandoConexao() throws IOException {
        //Faz o serverSocket esperar uma conexão, só da o retorno quando a conexão não é estabelecida
        System.out.println("Esperando a resposta do cliente .....");
        System.out.println("Fique atento se precisar dar permissão ao Firewall do seu Sistema Operacional");
        Socket socket = serverSocket.accept();
        System.out.println("conexão estabelecida com o cliente");
        return socket;
    }

}

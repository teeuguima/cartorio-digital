package comunicacao;

import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import facade.ServidorFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexao {

    private ServerSocket serverSocket;
    private int porta;
    private ConectionIO io;

    private final int PORTACARTORIO = 5555;

    private ServidorFacade facade;

    /* private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;

    public Conexao(ControllerDeTratamento tratamento, ControladorDeMensagens mensagens, ServerSocket server) {
        this.tratamento = tratamento;
        this.mensagens = mensagens;
        this.serverSocket = server;
    }
     */
    public Conexao() {

    }

    public Conexao(ServidorFacade facade) {
        this.facade = facade;
    }

    public void iniciar() throws IOException {
        // conectar();
        criarServerSocket();
        Socket socket = esperandoConexao();
        if (socket.isConnected()) {
            io = new ConectionIO(socket, facade);
        }
        //  io = new ConectionIO(socket,tratamento, mensagens);

    }

    public ConectionIO getConectionIO() {
        return io;
    }

    public ServerSocket getSocket() {
        return serverSocket;

    }

    public void criarServerSocket(/*int porta*/) throws IOException {
        //this.porta = porta;
        serverSocket = new ServerSocket(PORTACARTORIO);
    }

    public int getPorta() {
        return porta;
    }

    public Socket esperandoConexao() throws IOException {
        //Faz o serverSocket esperar uma conexão, só da o retorno quando a conexão não é estabelecida
        System.out.println("Esperando a resposta do cliente .....");
        System.out.println("Fique atento se precisar dar permição ao Firewall do seu Sistema Operacional");
        Socket socket = serverSocket.accept();
        System.out.println("conexão estabelecida com o cliente");
        return socket;
    }

    private void conectar() throws IOException {
        // criarServerSocket(PORTACARTORIO);
    }

}

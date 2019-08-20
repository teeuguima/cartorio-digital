package servidor;

import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import excecoes.SocketTratadoException;
import facade.ClienteServidorFacade;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;
import protocolo.Mensagem;

/**
 * Classe para tratamento de mensagens de clientes conectados ao servidor desta
 * aplicação
 *
 * @author Teeu Guima
 */
public class ConectionIO {

    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;
    private final ClienteServidorFacade facade;
    private Socket socket;

    public ConectionIO(Socket socket, ClienteServidorFacade facade) throws IOException {
        this.facade = facade;
        this.mensagens = new ControladorDeMensagens();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;

    }

    /**
     * Este método confere input e output do servidor. Primeiramente há
     * verificação de conexão e em seguida um loop para manter ativa a
     * verificação de entrada e saída.
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws SocketTratadoException
     */
    public void tratar() throws IOException, InterruptedException, FileNotFoundException, ClassNotFoundException, SocketTratadoException, InvalidKeySpecException {
        boolean flag = true;
        if (socket.isConnected()) {
            while (flag) {
                if (socket.getInputStream().available() > 0) {
                    tratarInput();
                    tratarOutput(socket.getOutputStream());
                    facade.armazenarDados();
                }
            }
        }

    }

     /**
     * Método para tratamento da saída/resposta ao cliente.
     *
     * @param output
     * @throws IOException
     * @throws SocketTratadoException
     */
    private void tratarOutput(OutputStream output) throws IOException, SocketTratadoException {
        if (mensagens.getMensagem().hasMensagem()) {
            System.out.println("Mensagem a");
            Mensagem mensagem = mensagens.getMensagem();
            byte[] bytes = mensagem.getBytes();
            output.write(bytes, 0, bytes.length);
            output.flush();
            mensagens.getMensagem().enviouMensagem();
            throw new SocketTratadoException();
        }
    }
    
   
    /**Método para verificação e tratamento da entrada
     * enviada por um cliente, a entrada é conferida e
     * respondida.
     *
     * @param input
     * @throws IOException
     */
    private void tratarInput() throws IOException, FileNotFoundException, ClassNotFoundException, InvalidKeySpecException {

        byte[] bytes = toByteArray(socket.getInputStream());
        if (bytes.length > 0) {
            tratamento.tratarMensagemCliente(bytes);
        }

    }
    
    /**Método conversor de InputStream em array de bytes, afim de
     * tratar a mensagem vinda de qualquer Sistema.
     * 
     * @param input
     * @return
     * @throws IOException 
     */
    private byte[] toByteArray(InputStream input) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(input);

        byte buffer[] = new byte[dataInputStream.available()];
        System.out.println(buffer.length);
        dataInputStream.readFully(buffer);

        return buffer;
    }

    public void fecharSocket(Socket socket) throws IOException {
        socket.close();
    }

}

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
import protocolo.Mensagem;

public class ConectionIO {

    //tratamento de mensagem e checagem de mensagem
    //  private final OutputStream output;
    //  private final InputStream input;
    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;
    private final ClienteServidorFacade facade;
    private Socket socket;

    /*
    public ConectionIO(Socket socket, ControllerDeTratamento tratamento, ControladorDeMensagens mensagens) throws IOException {
        this.tratamento = tratamento;
        this.mensagens = mensagens;
        this.socket = socket;
        output = socket.getOutputStream();
        input = socket.getInputStream();
    }
     */
    public ConectionIO(Socket socket, ClienteServidorFacade facade) throws IOException {
        this.facade = facade;
        this.mensagens = new ControladorDeMensagens();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;
        //  output = socket.getOutputStream();
        //  input = socket.getInputStream();
    }

    public void tratar() throws IOException, InterruptedException, FileNotFoundException, ClassNotFoundException, SocketTratadoException {
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
        //fecharSocket(socket);
    }

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

    private void tratarInput() throws IOException, FileNotFoundException, ClassNotFoundException {
        //  BufferedReader bufferRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        byte[] bytes = toByteArray(socket.getInputStream());
        if (bytes.length > 0) {
            String dados = new String(bytes, StandardCharsets.UTF_8);
           // System.out.println(dados);
            tratamento.tratarMensagemCliente(bytes);
        }

    }

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

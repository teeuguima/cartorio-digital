package comunicacao;

import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import excecoes.SocketTratadoException;
import facade.ServidorFacade;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class ConectionIO {

    //tratamento de mensagem e checagem de mensagem
    //  private final OutputStream output;
    //  private final InputStream input;
    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;
    private final ServidorFacade facade;
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
    public ConectionIO(Socket socket, ServidorFacade facade) throws IOException {
        this.mensagens = new ControladorDeMensagens();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;
        this.facade = facade;
        //  output = socket.getOutputStream();
        //  input = socket.getInputStream();
    }

    public void tratar() throws IOException, InterruptedException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, PerfilNaoCadastradoException, SocketTratadoException, PerfilCadastradoException, InvalidKeySpecException {
        boolean flag = true;
        if (socket.isConnected()) {
            while (flag) {
                if (socket.getInputStream().available() > 0) {
                    tratarInput();
                    tratarOutput(socket.getOutputStream());
                }
            }
            //fecharSocket(socket);
        }
    }

    private void tratarOutput(OutputStream output) throws IOException, SocketTratadoException {
        if (socket.isConnected()) {
            if (mensagens.getMensagem().hasMensagem()) {
                Mensagem mensagem = mensagens.getMensagem();
                byte[] bytes = mensagem.getBytes();
                output.write(bytes, 0, bytes.length);
                output.flush();
                mensagens.getMensagem().enviouMensagem();
                facade.armazenarDados();
                throw new SocketTratadoException();
                // fecharSocket(socket);
            } else {
            }
        }
    }

    private void tratarInput() throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, PerfilNaoCadastradoException, PerfilCadastradoException, InvalidKeySpecException {
        //  BufferedReader bufferRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        byte[] bytes = toByteArray(socket.getInputStream());
        if (socket.isConnected()) {
            if (bytes.length > 0) {
                String dados = new String(bytes, StandardCharsets.UTF_8);
                tratamento.tratarMensagem(bytes);
            }

        }
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(input);

        byte buffer[] = new byte[dataInputStream.available()];
        dataInputStream.readFully(buffer);

        return buffer;
    }

    public void fecharSocket(Socket socket) throws IOException {
        socket.close();
    }

}

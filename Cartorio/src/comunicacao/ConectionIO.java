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

/**
 * Classe responsável por tratar as conexões que o servidor(Cartório)
 * estabelece, verificando a informação de entrada e como saída uma resposta
 * sobre a requisição.
 *
 * @author Mateus Guimarães.
 */
public class ConectionIO {

    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;
    private final ServidorFacade facade;
    private Socket socket;

    public ConectionIO(Socket socket, ServidorFacade facade) throws IOException {
        this.mensagens = new ControladorDeMensagens();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;
        this.facade = facade;

    }

    /**
     * Este método confere input e output do servidor. Primeiramente há
     * verificação de conexão e em seguida um loop para manter ativa a
     * verificação de entrada e saída.
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws PerfilNaoCadastradoException
     * @throws SocketTratadoException
     * @throws PerfilCadastradoException
     * @throws InvalidKeySpecException
     */
    public void tratar() throws IOException, InterruptedException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, PerfilNaoCadastradoException, SocketTratadoException, PerfilCadastradoException, InvalidKeySpecException {
        boolean flag = true;
        if (socket.isConnected()) {
            while (flag) {
                if (socket.getInputStream().available() > 0) {
                    tratarInput();
                    tratarOutput(socket.getOutputStream());
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
        if (socket.isConnected()) {
            if (mensagens.getMensagem().hasMensagem()) {
                Mensagem mensagem = mensagens.getMensagem();
                byte[] bytes = mensagem.getBytes();
                output.write(bytes, 0, bytes.length);
                output.flush();
                mensagens.getMensagem().enviouMensagem();
                facade.armazenarDados();
                throw new SocketTratadoException();

            } else {
            }
        }
    }

    /**Método para verificação e tratamento da entrada
     * enviada por um cliente, a entrada é conferida e
     * respondida.
     *
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws PerfilNaoCadastradoException
     * @throws PerfilCadastradoException
     * @throws InvalidKeySpecException
     */
    private void tratarInput() throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, PerfilNaoCadastradoException, PerfilCadastradoException, InvalidKeySpecException {
        byte[] bytes = toByteArray(socket.getInputStream());

        if (bytes.length > 0) {
            tratamento.tratarMensagem(bytes); //Chamada ao método da classe responsável por tratar o input.
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
        dataInputStream.readFully(buffer);
        return buffer;
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import excecoes.SocketTratadoException;
import facade.ClienteServidorFacade;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import protocolo.Mensagem;

/**Classe responsável por tratar as conexões realizadas
 * com servidores/clientes, enviando e recebendo mensagens.
 *
 * @author Mateus Guimarães
 */
public class ConectionIO {

//    private final OutputStream output;
//    private final InputStream input;
    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;
    private final ClienteServidorFacade facade;
    private Socket socket;

    public ConectionIO(Socket socket, ClienteServidorFacade facade) throws IOException {
        this.mensagens = facade.getMensagem();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.facade = facade;
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
    public void tratar() throws IOException, InterruptedException, SocketTratadoException {
        boolean flag = true;
        if (socket.isConnected()) {
            while (flag) {
                tratarOutput(socket.getOutputStream());
                tratarInput(socket.getInputStream());
                facade.armazenarDados();
            }
        }
    }

    /**Método para verificação e tratamento da entrada
     * enviada por um servidor, a entrada é conferida e
     * respondida.
     *
     * @param input
     * @throws IOException
     */
    public void tratarInput(InputStream input) throws IOException {
        byte[] bytes = toByteArray(input);
        if (bytes.length > 0) {
            tratamento.tratarMensagemServidor(bytes);
        }
    }

    /**
     * Método para tratamento da saída/resposta para o cliente.
     *
     * @param output
     * @throws IOException
     * @throws SocketTratadoException
     */
    public void tratarOutput(OutputStream output) throws IOException, SocketTratadoException {
        if (mensagens.getMensagem().isStatus()) {
            if (mensagens.getMensagem().hasMensagem()) {
                Mensagem mensagem = mensagens.getMensagem();
                byte[] bytes = mensagem.getBytes();
                output.write(bytes, 0, bytes.length);
                output.flush();
                mensagem.enviouMensagem();
                throw new SocketTratadoException();
            }
        }else{
            fecharSocket();
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
    
    private void fecharSocket() throws IOException{
        this.socket.close();
    }
}

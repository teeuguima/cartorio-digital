/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import controller.ControladorDeMensagens;
import controller.ControllerDeTratamento;
import facade.ClienteServidorFacade;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import protocolo.Mensagem;

/**
 *
 * @author Teeu Guima
 */
public class ConectionIO {

    private final OutputStream output;
    private final InputStream input;

    private final ControllerDeTratamento tratamento;
    private final ControladorDeMensagens mensagens;

    private Socket socket;

    public ConectionIO(Socket socket, ClienteServidorFacade facade) throws IOException {
        this.mensagens = facade.getMensagem();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.socket = socket;
        output = socket.getOutputStream();
        input = socket.getInputStream();
    }

    public void tratar() throws IOException, InterruptedException {
        boolean flag = true;
        while (flag) {
            tratarOutput();
            tratarInput();
        }

    }

    public void tratarInput() throws IOException {
        byte[] bytes = toByteArray(socket.getInputStream());
        if (bytes.length > 0) {
            tratamento.tratarMensagemServidor(bytes);
        }
    }

    public void tratarOutput() throws IOException {
        if (mensagens.getMensagem().hasMensagem()) {
            Mensagem mensagem = mensagens.getMensagem();
            byte[] bytes = mensagem.getBytes();
            System.out.println(bytes.length);
            output.write(bytes, 0, bytes.length);
            output.flush();
            //output.close();
            mensagem.enviouMensagem();

        }
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(input);

        byte buffer[] = new byte[dataInputStream.available()];
        dataInputStream.readFully(buffer);

        return buffer;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import controller.ControladorDeMensagens;
import controller.ControllerDeTratamento;
import facade.ClienteServidorFacade;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import protocolo.Mensagem;

/**
 *
 * @author Teeu Guima
 */
public class TratamentoServidor extends Thread{
    private ServerThread serverTh;
    private Socket socket;
    private PrintWriter printWriter;
    private ControllerDeTratamento tratamento;
    private ControladorDeMensagens mensagens;
    private OutputStream output;
    private InputStream input;
    
    
    
    

    public TratamentoServidor(Socket socket, ClienteServidorFacade facade, ServerThread serverTh) throws IOException {

        this.mensagens = facade.getMensagem();
        this.tratamento = new ControllerDeTratamento(facade, mensagens);
        this.output = socket.getOutputStream();
        this.input = socket.getInputStream();
        this.serverTh = serverTh;
    }

    public void tratarInput(InputStream input) throws IOException {
        byte[] bytes = toByteArray(input);
        if (bytes.length > 0) {
            tratamento.tratarMensagemCliente(bytes);
        }
    }

    public void tratarOutput(OutputStream output) throws IOException {
        if (mensagens.getMensagem().hasMensagem()) {
            Mensagem mensagem = mensagens.getMensagem();
            byte[] bytes = mensagem.getBytes();
            output.write(bytes, 0, bytes.length);
            output.close();
            mensagem.enviouMensagem();
        }
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(input);

        byte buffer[] = new byte[dataInputStream.available()];
        dataInputStream.readFully(buffer);

        return buffer;
    }
    
    @Override
    public void run(){
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while(true){
                serverTh.sendMessage(buff.readLine());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public PrintWriter getPrintWriter(){
        return printWriter;
    } 
    
}

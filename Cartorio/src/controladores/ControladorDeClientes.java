package controladores;

import comunicacao.ConectionIO;
import comunicacao.Conexao;

import java.io.IOException;
import java.net.ServerSocket;

public class ControladorDeClientes {

    private Conexao pessoaFis;

    public ControladorDeClientes() {
    }

    public void iniciarClientePessoaFis(ControllerDeTratamento tratamento, ControladorDeMensagens mensagens, ServerSocket server) throws IOException {
      //  pessoaFis = new Conexao(tratamento, mensagens, server);
        pessoaFis.iniciar();
    }

    public ConectionIO getConectionIOPessoaFis() {
        return pessoaFis.getConectionIO();
    }

}

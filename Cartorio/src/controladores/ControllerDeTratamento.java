/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import excecoes.DadosIncorretosException;
import excecoes.LoginRealizadoException;
import excecoes.PerfilNaoCadastradoException;
import facade.ServidorFacade;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class ControllerDeTratamento {

    private ServidorFacade facade;
    private ControladorDeMensagens mensagem;
    private boolean rodandoCorrida;
    private String curTag;

    public ControllerDeTratamento(ServidorFacade facade, ControladorDeMensagens mensagem) {
        this.mensagem = mensagem;
        this.facade = facade;
    }

    public byte[] convertToByte(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public String convertToString(byte[] dados) {
        return new String(dados, StandardCharsets.UTF_8);
    }

    public void respostaCliente(JSONObject resposta) {
        String info = resposta.toString();
        //BufferedReader buffRead = new BufferedReader();
        //byte[] bytes = convertToByte(info);
        mensagem.novaMensagem(info);
    }

    public void tratarMensagem(byte[] bytes) throws IOException, FileNotFoundException, ClassNotFoundException {

        String info = new String(bytes, StandardCharsets.UTF_8);
        
        System.out.println(info);
        JSONObject dados = new JSONObject(info);

        switch (dados.getString("command")) {
            case "CadastroPerfil":
                try {
                    facade.cadastrarPerfil(dados.getString("nome"), dados.getString("sobrenome"), dados.getString("cpf"),
                            dados.getString("rg"), dados.getString("email"), dados.getString("telefone"), dados.getString("senha"));
                    dados.put("status", "Cadastro Efetuado!");
                    respostaCliente(dados);
                } catch (Exception e) {
                }
                break;
            case "Login":
                try {
                    facade.realizarLogin(dados.getString("email"), "senha");
                } catch (LoginRealizadoException | DadosIncorretosException | PerfilNaoCadastradoException e) {
                   // dados.put("status", e.toString());
                }

                break;
        }
        facade.armazenarDados();

    }

}

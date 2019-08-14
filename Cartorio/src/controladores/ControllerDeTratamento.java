/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import excecoes.DadosIncorretosException;
import excecoes.DocumentoCadastradoException;
import excecoes.LoginRealizadoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import facade.ServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import org.json.JSONException;
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
        byte[] bytes = convertToByte(info);
        System.out.println("Enviando...");
        mensagem.novaMensagem(bytes);
    }
    
    public String exceptionString(String exception){
        String[] result = exception.split(": ");
        return result[1];
    }

    public void tratarMensagem(byte[] bytes) throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        String info = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("Entrou...");
        System.out.println(info);
        JSONObject dados = new JSONObject(info);

        switch (dados.getString("command")) {
            case "CadastroPerfil":
                try {
                    facade.cadastrarPerfil(dados.getString("nome"), dados.getString("sobrenome"), dados.getString("cpf"),
                            dados.getString("rg"), dados.getString("email"), dados.getString("telefone"), dados.getString("senha"));
                } catch (PerfilCadastradoException | NoSuchAlgorithmException | JSONException e) {
                    dados.put("status", exceptionString(e.toString()));
                    respostaCliente(dados);
                }
                break;
            case "Login":
                try {
                    facade.realizarLogin(dados.getString("cpf"), dados.getString("senha"));
                } catch (LoginRealizadoException | DadosIncorretosException | PerfilNaoCadastradoException e) {
                   dados.put("status",exceptionString(e.toString()));
                   respostaCliente(dados);
                }
                break;
            case "CadastrarDocumento":
                try{
                    facade.cadastrarDocumento(dados.getString("cpf"), convertToByte(dados.getString("documento")));
                    
                }catch( NoSuchAlgorithmException | InvalidKeyException | SignatureException | DocumentoCadastradoException e){
                    dados.put("status", exceptionString(e.toString()));
                    respostaCliente(dados);
                }
                break;
            case "TransferirDocumento":
        }
        facade.armazenarDados();

    }

}

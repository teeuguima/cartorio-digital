/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;


import facade.ClienteServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class ControllerDeTratamento {

    private ClienteServidorFacade facade;
    private ControladorDeMensagens mensagem;

    public ControllerDeTratamento(ClienteServidorFacade facade, ControladorDeMensagens mensagem) {
        this.facade = facade;
        this.mensagem = mensagem;
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
    }

    public void tratarMensagemServidor(byte[] bytes){

        String info = new String(bytes, StandardCharsets.UTF_8);
        JSONObject dados = new JSONObject(info);
        facade.setRespostaCartorio(dados);
        
        //facade.armazenarDados();

    }
    
    public void tratarMensagemCliente(byte[] bytes){
        String info = new String(bytes, StandardCharsets.UTF_8);
        JSONObject dados = new JSONObject(info);
        switch(dados.getString("command")){
            case "Mensagem":
                System.out.println(dados.toString());
                break;
        }
        
    }

    
}

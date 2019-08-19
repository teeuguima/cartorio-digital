/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import facade.ClienteServidorFacade;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import model.Documento;
import org.json.JSONObject;
import util.ConvertKey;

public class ControllerDeTratamento {

    private ClienteServidorFacade facade;
    private ControladorDeMensagens mensagem;
    private ConvertKey convert;

    public ControllerDeTratamento(ClienteServidorFacade facade, ControladorDeMensagens mensagem) {
        this.facade = facade;
        this.mensagem = mensagem;
        this.convert = new ConvertKey();
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
        mensagem.novaMensagem(bytes);
    }

    public void tratarMensagemServidor(byte[] bytes) {

        String info = new String(bytes, StandardCharsets.UTF_8);
        JSONObject dados = new JSONObject(info);
        facade.setRespostaCartorio(dados);

        //facade.armazenarDados();
    }

    public void tratarMensagemCliente(byte[] bytes) {
        String info = new String(bytes, StandardCharsets.UTF_8);
        JSONObject dados = new JSONObject(info);
        switch (dados.getString("command")) {
            case "SolicitarDocumento":

                Documento doc = facade.solicitarDocumento(dados.getInt("idDoc"));
                JSONObject json = new JSONObject();
                json.put("cpfDono", doc.getCpf());
                json.put("chavePublica", convert.converterPublicKey(doc.getPbKey()));
                json.put("assinatura", convertToString(doc.getAssinatura()));
                json.put("arquivo", convertToString(doc.getArquivo()));
                respostaCliente(json);
                break;
            case "TransferirDocumento":
                try {
                    doc = facade.transferirPosseDeDocumento(dados.getInt("idDocumento"), dados.getString("cpf"));
                    dados.put("documento", (Object) doc);

                    respostaCliente(dados);

                    JSONObject requisicao = new JSONObject();
                    requisicao.put("command", "RemoverDocumento");
                    requisicao.put("cpf", facade.getPerfil());
                    requisicao.put("privateKey", (Object) facade.getPerfil().getPvKey());
                    facade.novaMensagem(convertToByte(dados.toString()));

                } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException ex) {
                    System.out.println(ex.toString());
                }

                break;
        }

    }

}

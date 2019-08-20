package controladores;

import excecoes.AutenticidadeDoDocumentoException;
import excecoes.DocumentoCadastradoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import facade.ServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import model.Documento;
import model.Perfil;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ConvertKey;

/**Classe responsável por todo o tratamento de entrada
 * do servidor (Cartório), utilizando a classe Facade
 * para intermediar as operações do Controller - ControladorDeDados.
 * Responde as requisições utilizando JSONObject.
 * 
 * @author Mateus Guimarães
 */
public class ControllerDeTratamento {

    private ServidorFacade facade;
    private ControladorDeMensagens mensagem;
    private ConvertKey convert;
    
    public ControllerDeTratamento(ServidorFacade facade, ControladorDeMensagens mensagem) {
        this.mensagem = mensagem;
        this.facade = facade;
        this.convert = new ConvertKey();
    }

    /**Método que converte uma string em array de byte
     * 
     * @param string
     * @return Array de byte.
     */
    public byte[] convertToByte(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }
    
    /**Método para conversão de um array de byte em String
     * 
     * @param dados
     * @return String 
     */
    public String convertToString(byte[] dados) {
        return new String(dados, StandardCharsets.UTF_8);
    }
    
    /**Método para que a classe de controle de mensagens
     * avise a classe de tratamento da conexão que há respostas
     * ao cliente conectado.
     * 
     * @param resposta 
     */
    public void respostaCliente(JSONObject resposta) {
        String info = resposta.toString();
        byte[] bytes = convertToByte(info);
        mensagem.novaMensagem(bytes);
    }
    
    /**Método que captura a exceção e trata,
     * retornando a informação principal.
     * 
     * @param exception
     * @return String
     */
    public String exceptionString(String exception) {
        String[] result = exception.split(": ");
        return result[1];
    }
    
    /**Método fundamental para o controle da comunicação
     * destinando adequadamente cada operação desejada
     * pelo cliente e retornando com êxito as respostas 
     * adquiridas pelo Controlador de Dados.
     * 
     * @param bytes
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
    public void tratarMensagem(byte[] bytes) throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, PerfilNaoCadastradoException, PerfilCadastradoException, InvalidKeySpecException {

        String info = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(info);
        JSONObject dados = new JSONObject(info);

        switch (dados.getString("command")) {
            case "CadastroPerfil":

                String[] keys = facade.cadastrarPerfil(dados.getString("nome"), dados.getString("sobrenome"), dados.getString("cpf"),
                        dados.getString("rg"), dados.getString("email"), dados.getString("telefone"), dados.getString("senha"));
                if (keys[0].compareTo("Perfil já cadastrado!") == 0) {
                    dados.put("status", "Perfil já cadastrado!");
                    respostaCliente(dados);
                } else {
                    dados.put("chavePrivada", keys[0]);
                    dados.put("chavePublica", keys[1]);
                    dados.put("status", "Perfil cadastrado com sucesso!");
                    respostaCliente(dados);
                }

                break;
            case "Login":
                Perfil p = facade.realizarLogin(dados.getString("cpf"), dados.getString("senha"));
                if (p != null) {
                    dados.put("status","Login efetuado com sucesso!");
                    dados.put("nome", p.getNome());
                    dados.put("sobrenome", p.getSobrenome());
                    dados.put("cpf", p.getCpf());
                    dados.put("chavePublica", convert.converterPublicKey(p.getPbKey()));
                    dados.put("chavePrivada", convert.converterPrivateKey(p.getPvKey()));
                    respostaCliente(dados);
                }else{
                    dados.put("status", "Dados inválidos ou perfil não cadastrado!");
                    respostaCliente(dados);
                }

                break;
            case "CadastrarDocumento":
                try {
                    facade.cadastrarDocumento(dados.getString("cpf"), convertToByte(dados.getString("documento")));
                    
                } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | DocumentoCadastradoException | PerfilNaoCadastradoException e) {
                    dados.put("status", exceptionString(e.toString()));
                    respostaCliente(dados);
                }
                break;
            case "ValidarDocumento":
                try {
                    facade.validarDocumento(dados.getString("cpfDono"), dados.getString("chavePublica"), convertToByte(dados.getString("arquivo")), convertToByte(dados.getString("assinatura")));
                    dados.put("status", "Erro!!!");
                    respostaCliente(dados);
                } catch (AutenticidadeDoDocumentoException | PerfilNaoCadastradoException e) {
                    dados.put("status", exceptionString(e.toString()));
                    respostaCliente(dados);
                }
                break;
            case "BuscarDocumento":
                ArrayList<Documento> docs = facade.buscarDocumento(dados.getString("cpf"));
                Iterator iterDocs = docs.iterator();
                JSONArray arrayJson = new JSONArray();
                int i = 0;
                while (iterDocs.hasNext()) {
                    Documento doc = (Documento) iterDocs.next();
                    JSONObject json = new JSONObject();
                    json.put("id", doc.getId());
                    json.put("documento", convertToString(doc.getArquivo()));
                    arrayJson.put(i, (Object) json);
                    i++;
                }
                dados.put("documentos", arrayJson);
                respostaCliente(dados);
                break;
            case "RemoverDocumento":
                facade.removerDocumento(dados.getString("cpf"), (PublicKey) dados.get("pbKey"), convertToByte(dados.getString("assinatura")));
                break;
        }
        facade.armazenarDados();

    }

}

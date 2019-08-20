package facade;

import controladores.ControladorDeDados;
import controladores.ControladorDeMensagens;
import controladores.ControladorFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import model.Documento;
import model.Perfil;
import org.json.JSONObject;

/**Classe que tem como função intermédio entre os controladores e a classe principal!
 * 
 * @author Mateus Guimarães
 */
public class ClienteServidorFacade {

    private final ControladorDeDados dados;
    private final ControladorFactory factory;
    private final ControladorDeMensagens mensagem;
    private static ClienteServidorFacade facade;
    private JSONObject jsonCartorio;
    private JSONObject jsonCliente;

    public ClienteServidorFacade(ControladorDeMensagens ctrlMessages) throws IOException, FileNotFoundException, ClassNotFoundException {
        dados = ControladorDeDados.getInstance();
        factory = new ControladorFactory();
        mensagem = ctrlMessages;
    }

    /*
    public static synchronized ClienteServidorFacade getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (facade == null) ? facade = new ClienteServidorFacade() : facade;
    }
     */
    public void cadastrarPerfil(String nome, String sobrenome, String cpf, PrivateKey pvKey, PublicKey pbKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.dados.cadastrarPerfil(factory.factoryPerfil(nome, sobrenome, cpf, pvKey, pbKey));
    }

    public void CadastrarDocumento(byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        this.dados.cadastrarDocumento(documento);
    }

    public Documento solicitarDocumento(int idDoc) {
        return this.dados.documentoSolicitado(idDoc);
    }

    public Documento transferirPosseDeDocumento(int idDocumento, String cpf, PublicKey pbKey, byte[] assinatura) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        return this.dados.transferirDocumento(idDocumento, cpf, pbKey, assinatura);
    }

    public void setPerfil(Perfil p) {
        this.dados.setPerfil(p);
    }

    public Perfil getPerfil() {
        return dados.getPerfil();
    }

    /**
     * CADASTRO DE DOCUMENTOS NA MEMÓRIA LOCAL!
     *
     */
    /**
     * PERSISTÊNCIA DE DADOS
     *
     * @throws IOException
     */
    public void criandoArquivos() throws IOException {
        dados.criandoArquivos();
    }

    public void lerDados() throws IOException, FileNotFoundException, ClassNotFoundException {
        dados.lendoDados();
    }

    public void armazenarDados() throws IOException {
        dados.salvandoDados();
    }

    /**
     * *************************** MÉTODOS PARA A COMUNICAÇÃO
     *
     * @param bytes
     */
    public void novaMensagem(byte[] bytes) {
        mensagem.novaMensagem(bytes);
    }

    public void fecharConexaoCartorio() {
        mensagem.fecharConexao();
    }

    public void abrirConexaoCartorio() {
        mensagem.abrirConexao();
    }

    public void novaMensagem(String message) {
        System.out.println("Passou");
        mensagem.novaMensagem(message);
    }

    public JSONObject getRespostaCliente() throws InterruptedException {
        Thread.sleep(1500);
        return this.jsonCliente;
    }

    public void setRespostaCliente(JSONObject json) {
        this.jsonCliente = json;
    }

    public JSONObject getRespostaCartorio() throws InterruptedException {
        Thread.sleep(1500);
        return this.jsonCartorio;
    }

    public void setRespostaCartorio(JSONObject json) {
        this.jsonCartorio = json;
    }

    public ControladorDeMensagens getMensagem() {
        return mensagem;
    }

}

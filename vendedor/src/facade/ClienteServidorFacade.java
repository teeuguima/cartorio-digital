package facade;



import controladores.ControladorDeDados;
import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import controladores.ControladorFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONObject;

public class ClienteServidorFacade {

    private final ControladorDeDados dados;
    private final ControladorFactory factory;
    private final ControladorDeMensagens mensagem;
    private static ClienteServidorFacade facade;
    private JSONObject jsonCartorio;
    private JSONObject jsonCliente;
    /**
     * Méodo construtor se inicializa instanciando cada um os controladores.
     * Essa classe é o que vai acessar todos os controladores e vai ser a classe
     * disponível para quem quiser acessar o programa. As operações só serão
     * feitas a partir desta classe, e nenhum controlador vai ser acessado senão
     * por ela.
     */
    public ClienteServidorFacade() {
        dados = new ControladorDeDados();
        factory = new ControladorFactory();
        mensagem = new ControladorDeMensagens();
    }

    public static synchronized ClienteServidorFacade getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (facade == null) ? facade = new ClienteServidorFacade() : facade;
    }

    

    /**PERSISTÊNCIA DE DADOS
     * 
     * @throws IOException 
     */   
    public void criandoArquivos() throws IOException {
        dados.criandoArquivos();
    }
/*
    public void lerDados() throws IOException, FileNotFoundException, ClassNotFoundException {
        dados.lendoDados();
    }

    public void armazenarDados() throws IOException {
        dados.salvandoDados();
    }
*/
    /**
     * *************************** MÉTODOS PARA A COMUNICAÇÃO
     *
     * @param bytes
     */
    public void novaMensagem(byte[] bytes){
        mensagem.novaMensagem(bytes);
    }
    
    public void fecharConexao(){
        mensagem.fecharConexao();
    }
    
    public void novaMensagem(String message){
        System.out.println("Passou");
        mensagem.novaMensagem(message);
    }
    
    public JSONObject getRespostaCartorio() throws InterruptedException{
        Thread.sleep(1500);
        return this.jsonCartorio;
    }
    
    public void setRespostaCartorio(JSONObject json){
        this.jsonCartorio = json;
    }
    
    public JSONObject getRespostaCliente(){
        return this.jsonCliente;
    }
    
    public void setRespostaCliente(JSONObject json){
        this.jsonCliente = json;
    }
    
    
    public ControladorDeMensagens getMensagem() {
        return mensagem;
    }
    
    
}

package facade;



import controller.ControladorDeDados;
import controller.ControladorDeMensagens;
import controller.ControllerDeTratamento;
import controller.ControladorFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONObject;

public class ClienteServidorFacade {

    private final ControladorDeDados dados;
    private final ControladorFactory factory;
    private final ControladorDeMensagens mensagem;
    private static ClienteServidorFacade facade;
    private JSONObject json;
    /**
     * Méodo construtor se inicializa instanciando cada um os controladores.
     * Essa classe é o que vai acessar todos os controladores e vai ser a classe
     * disponível para quem quiser acessar o programa. As operações só serão
     * feitas a partir desta classe, e nenhum controlador vai ser acessado senão
     * por ela.
     */
    public ClienteServidorFacade() {
        dados = ControladorDeDados.getInstance();
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
    public void novaMensagem(byte[] bytes){
        mensagem.novaMensagem(bytes);
    }
    
    public void novaMensagem(String message){
        System.out.println("Passou");
        mensagem.novaMensagem(message);
    }
    
    public JSONObject getRespostaCartorio() throws InterruptedException{
        Thread.sleep(2000);
        return this.json;
    }
    
    public void setRespostaCartorio(JSONObject json){
        this.json = json;
    }
    
    
    public ControladorDeMensagens getMensagem() {
        return mensagem;
    }
    
    
}

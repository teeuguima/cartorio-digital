package facade;

import comunicacao.ConectionIO;
import java.util.ArrayList;
import controladores.ControladorDeClientes;
import controladores.ControladorDeDados;
import controladores.ControladorDeMensagens;
import controladores.ControllerDeTratamento;
import controladores.ControladorFactory;
import excecoes.DadosIncorretosException;
import excecoes.DocumentoCadastradoException;
import excecoes.LoginRealizadoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import model.Perfil;

public class ServidorFacade {

    private final ControladorDeDados dados;
    private final ControladorFactory factory;
    private final ControladorDeClientes clientes;
    private static ServidorFacade facade;

    /**
     * Méodo construtor se inicializa instanciando cada um os controladores.
     * Essa classe é o que vai acessar todos os controladores e vai ser a classe
     * disponível para quem quiser acessar o programa. As operações só serão
     * feitas a partir desta classe, e nenhum controlador vai ser acessado senão
     * por ela.
     */
    public ServidorFacade() throws IOException, FileNotFoundException, ClassNotFoundException {
        dados = new ControladorDeDados();
        factory = new ControladorFactory();
        clientes = new ControladorDeClientes();
    }

    public static synchronized ServidorFacade getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (facade == null) ? facade = new ServidorFacade() : facade;
    }

    public void cadastrarPerfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, String senha) throws PerfilCadastradoException, NoSuchAlgorithmException, UnsupportedEncodingException {
        this.dados.cadastrarPerfil(factory.factoryPerfil(nome, sobrenome, cpf, rg, email, telefone, dados.cadastrarSenha(senha)));
    }

    public void realizarLogin(String cpf, String senha) throws LoginRealizadoException, DadosIncorretosException, PerfilNaoCadastradoException, NoSuchAlgorithmException, UnsupportedEncodingException {
        this.dados.realizarLogin(cpf, senha);
    }
    
    public void cadastrarDocumento(String cpf, byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DocumentoCadastradoException{
        dados.cadastrarDocumento(cpf, documento);
    }

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
     * @param server
     * @throws java.io.IOException *******************************
     */
}

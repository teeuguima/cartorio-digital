package facade;

import java.util.ArrayList;

import controladores.ControladorDeDados;
import controladores.ControladorFactory;
import excecoes.AutenticidadeDoDocumentoException;
import excecoes.DocumentoCadastradoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import model.Documento;
import model.Perfil;
import util.ConvertKey;

public class ServidorFacade {

    private final ControladorDeDados dados;
    private final ControladorFactory factory;
;
    private static ServidorFacade facade;
    private ConvertKey convert;

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
        convert = new ConvertKey();
    }

    public static synchronized ServidorFacade getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (facade == null) ? facade = new ServidorFacade() : facade;
    }

    public String[] cadastrarPerfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, String senha) throws PerfilCadastradoException, NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyPair keyP = dados.chavesDeSeguranca(cpf);
        boolean flag = this.dados.cadastrarPerfil(factory.factoryPerfil(nome, sobrenome, cpf, rg, email, telefone,
                dados.cadastrarSenha(senha), dados.geradorDeHash(cpf), keyP.getPrivate(), keyP.getPublic()));
        String[] keys = new String[2];
        if (flag) {
            keys[0] = convert.converterPrivateKey(keyP.getPrivate());
            keys[1] = convert.converterPublicKey(keyP.getPublic());
            return keys;
        } else {
            String[] status = new String[1];
            status[1] = "Perfil já cadastrado!";
            return status;
        }
    }

    public void validarDocumento(String cpf, String pbKey, byte[] arquivo, byte[] assinatura) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, AutenticidadeDoDocumentoException, PerfilNaoCadastradoException, InvalidKeySpecException {
        dados.validarDocumento(cpf, convert.convertStringToPublicKey(pbKey), arquivo, assinatura);
    }

    public Perfil realizarLogin(String cpf, String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return this.dados.realizarLogin(cpf, senha);
    }

    public void removerDocumento(String cpf, PublicKey pbKey, byte[] assinatura) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        this.dados.removerDocumento(cpf, pbKey, assinatura);
    }

    public void cadastrarDocumento(String cpf, byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DocumentoCadastradoException, UnsupportedEncodingException, PerfilNaoCadastradoException {
        this.dados.cadastrarDocumento(cpf, documento);
    }

    public ArrayList<Documento> buscarDocumento(String cpf) throws PerfilNaoCadastradoException {
        ArrayList<Documento> docs = this.dados.buscarDocumento(cpf);
        Iterator iterDocs = docs.iterator();

        while (iterDocs.hasNext()) {
            Documento doc = (Documento) iterDocs.next();
        }
        return docs;
    }

    /**
     * PERSISTÊNCIA DE DADOS
     *
     * @throws IOException
     */
    public void criandoArquivos() throws IOException, FileNotFoundException, ClassNotFoundException {
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

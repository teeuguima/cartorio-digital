package controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import model.Documento;
import model.Perfil;

/**
 * Classe responsável por armazenar e realizar as operações internas sobre as
 * requisições dos clientes e do usuário em execução.
 *
 * @author Mateus Guimarães
 */
public class ControladorDeDados {

    private Perfil perfil;
    private ArrayList<Documento> docs;
    private Documento doc_para_autenticacao;
    private final String ALGORITHM = "DSA";
    private static ControladorDeDados ctrlDados;
    File filePerfil;
    File fileDocs;

    public ControladorDeDados() {
        this.docs = new ArrayList<>();
        this.filePerfil = null;
        this.fileDocs = null;
    }

    /**
     * Método para sincronizar o uso da classe pelas Threads em execução.
     *
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public static synchronized ControladorDeDados getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (ctrlDados == null) ? ctrlDados = new ControladorDeDados() : ctrlDados;
    }

    /**
     * Método responsável por conferir se um documento no formato de array de
     * byte já foi inserido no sistema!
     *
     * @param documento
     * @return boolean, se há um documento cadastrado retorna true, senão false.
     */
    public boolean hasDocumento(byte[] documento) {
        Iterator iterDocs = docs.iterator();
        while (iterDocs.hasNext()) {
            Documento doc = (Documento) iterDocs.next();
            if (Arrays.equals(doc.getArquivo(), documento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método responsável por gerar chaves públicas e privadas a partir do cpf
     * do cliente, gerando uma sequência aleatória com o array de bytes desta
     * string.
     *
     * @param cpf
     * @return KeyPair, contendo a chave pública e privada.
     * @throws NoSuchAlgorithmException
     */
    public KeyPair chavesDeSeguranca(String cpf) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secRand = new SecureRandom(cpf.getBytes());
        kpg.initialize(512, secRand);

        KeyPair keyP = kpg.generateKeyPair();
        return keyP;
    }

    /**
     * Método que gera uma hash utilizando o cpf do cliente, retornando a hash a
     * ser carimbada na inscrição de um documento.
     *
     * @param cpf
     * @return Array de byte, contendo a hash baseada no cpf do cliente.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public byte[] geradorDeHash(String cpf) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte hashPosse[] = md.digest(cpf.getBytes("UTF-8"));
        return hashPosse;
    }

    /**
     * Método para inserir o perfil do utilizador do sistema em questão.
     *
     * @param perfil
     */
    public void cadastrarPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * Getter para retornar o utilizador do sistema!
     *
     * @return Perfil
     */
    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil p) {
        this.perfil = p;
    }

    /**
     * Método para cadastrar um documento de forma local, após o mesmo ter sido
     * cadastrado no servidor.
     *
     * @param documento
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public void cadastrarDocumento(byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sigDono = Signature.getInstance(ALGORITHM);

        sigDono.initSign(perfil.getPvKey());
        sigDono.update(documento);
        byte[] assinatura = sigDono.sign();

        Documento doc = new Documento(perfil.getCpf(), perfil.getPbKey(), documento, assinatura, geradorDeHash(perfil.getCpf()));

        docs.add(doc);
    }

    /**
     * Método que busca o documento cadastrado localmente para retornar ao
     * cliente que requisitou.
     *
     * @param idDoc
     * @return Documento
     */
    public Documento documentoSolicitado(int idDoc) {
        Iterator iterDocs = docs.iterator();
        while (iterDocs.hasNext()) {
            Documento doc = (Documento) iterDocs.next();
            if (doc.getId() == idDoc) {
                return doc;
            }
        }
        return null;
    }

    /**
     * Método responsável por transferir documento para um novo dono, conferindo
     * a existência deste dono no cartório. A substituição de posso é realizada
     * em um método interno do documento, que recebe a chave privada, publica,
     * assinatura e a hash do novo dono.
     *
     * @param idDoc
     * @param cpf
     * @param pbKey
     * @param assinatura
     * @return Documento
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws java.io.UnsupportedEncodingException
     */
    public Documento transferirDocumento(int idDoc, String cpf, PublicKey pbKey, byte[] assinatura) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {

        Signature signDono = Signature.getInstance(ALGORITHM);
        KeyPair keyP = chavesDeSeguranca(cpf);
        Iterator iterDocs = docs.iterator();
        while (iterDocs.hasNext()) {
            Documento doc = (Documento) iterDocs.next();
            if (doc.getId() == idDoc) {
                doc.addDonosAnteriores(doc.getAssinatura());
                signDono.initSign(keyP.getPrivate());
                signDono.update(doc.getArquivo());
                doc.setCpf(cpf);
                doc.setPbKey(pbKey);
                doc.setAssinatura(signDono.sign());
                doc.setHashDono(geradorDeHash(cpf));
                return doc;
            }
        }
        return null;

    }

    
     /**Método responsável por atualizar o arquivo de persistência de dados!
     * O mesmo atualiza o arquivo com os novos dados inseridos nas estruturas.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void salvandoDados() throws FileNotFoundException, IOException {
        if (this.perfil != null) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePerfil));
            output.writeObject(this.perfil);
            output.close();
        }
        if (this.docs != null && this.docs.size() > 0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileDocs));
            output.writeObject(this.docs);
            output.close();
        }

    }

    /**Método responsável por ler os dados armazenados nos arquivos e sobrescrever!
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void lendoDados() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (fileDocs.length() != 0) {
            ObjectInputStream inputDocs = new ObjectInputStream(new FileInputStream(fileDocs));
            if (this.docs.isEmpty()) {
                this.docs = (ArrayList<Documento>) inputDocs.readObject();
                inputDocs.close();
            }
        }
        if (filePerfil.length() != 0) {
            ObjectInputStream inputDocs = new ObjectInputStream(new FileInputStream(filePerfil));
            if (this.perfil == null) {
                this.perfil = (Perfil) inputDocs.readObject();
                inputDocs.close();
            }
        }
    }

    /**Método responsável por criar os arquivos de textos essenciais
     * para a persistência de dados do servidor (Cartório).
     * 
     * @throws IOException
     * @throws FileNotFoundException 
     */
    public void criandoArquivos() throws IOException {
        filePerfil = new File("fileperfis.txt");
        if (!filePerfil.exists()) {
            filePerfil = new File("fileperfis.txt");
        }

        fileDocs = new File("filedocs.txt");
        if (!fileDocs.exists()) {
            fileDocs = new File("filedocs.txt");
        }
    }

}

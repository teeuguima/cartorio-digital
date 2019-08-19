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
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import model.Documento;
import model.Perfil;

/**
 * O controlador de dados controla os dados cadastráveis do sistema. Essa classe
 * na segunda versão ganhará um banco de dados.
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
    
    public static synchronized ControladorDeDados getInstance() throws IOException, FileNotFoundException, ClassNotFoundException {
        return (ctrlDados == null) ? ctrlDados = new ControladorDeDados() : ctrlDados;
    }

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

    public KeyPair chavesDeSeguranca(String cpf) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secRand = new SecureRandom(cpf.getBytes());
        kpg.initialize(512, secRand);

        KeyPair keyP = kpg.generateKeyPair();
        return keyP;
    }
    
    public void armazenarDocumento(Documento doc){
        this.doc_para_autenticacao = doc;
    }

    public byte[] geradorDeHash(String cpf) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte hashPosse[] = md.digest(cpf.getBytes("UTF-8"));
        return hashPosse;
    }

    public void cadastrarPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }
    
    public void setPerfil(Perfil p) {
        this.perfil = p;
    }

    public void cadastrarDocumento(byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sigDono = Signature.getInstance(ALGORITHM);

        sigDono.initSign(perfil.getPvKey());
        sigDono.update(documento);
        byte[] assinatura = sigDono.sign();

        Documento doc = new Documento(perfil.getCpf(), perfil.getPbKey(), documento, assinatura, geradorDeHash(perfil.getCpf()));

        docs.add(doc);
    }
    
    public Documento documentoSolicitado(int idDoc){
        Iterator iterDocs = docs.iterator();
        while(iterDocs.hasNext()){
            Documento doc = (Documento)iterDocs.next();
            if(doc.getId() == idDoc){
                return doc;
            }
        }
        return null;
    }

    public Documento transferirDocumento(int idDoc, String cpf) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {

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
                doc.setPbKey(keyP.getPublic());
                doc.setAssinatura(signDono.sign());
                doc.setHashDono(geradorDeHash(cpf));
                return doc;
            }
        }
        return null;

    }

    public KeyPairGenerator geradorDeChave() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secRand = new SecureRandom();
        kpg.initialize(512, secRand);

        return kpg;
    }

    public void salvandoDados() throws FileNotFoundException, IOException {
        if(this.perfil != null){
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePerfil));
            output.writeObject(this.perfil);
            output.close();
        }
        if (this.docs != null && this.docs.size()>0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileDocs));
            output.writeObject(this.docs);
            output.close();
        }

    }
    
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

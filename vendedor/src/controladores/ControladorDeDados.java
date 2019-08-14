package controladores;


import facade.ClienteServidorFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import model.Documento;
import model.Perfil;


/**
 * O controlador de dados controla os dados cadastráveis do sistema. Essa classe
 * na segunda versão ganhará um banco de dados.
 */
public class ControladorDeDados {
    private Perfil perfis;
    private ArrayList<Documento> docs;

    File filePerfis = null;
    File fileDocs = null;
    
    public ControladorDeDados() {
        this.docs = new ArrayList<>();
    }

    public void cadastrarDocumento(Perfil perfil, byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sigDono = Signature.getInstance("DSA");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secRand = new SecureRandom();

        kpg.initialize(512, secRand);
        KeyPair keyP = kpg.generateKeyPair();

        PublicKey pbKey = keyP.getPublic();
        PrivateKey pvKey = keyP.getPrivate();

        sigDono.initSign(pvKey);
        sigDono.update(documento);
        byte[] assinatura = sigDono.sign();

        Documento doc = new Documento(perfil, pbKey, pvKey, documento, assinatura);
        docs.add(doc);
    }
/*
    public void transferirDocumento(String cpfDonoAnterior, String cpfNovoDono, PrivateKey pvKey, byte[] arquivo,
        byte[] assinatura) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signDono = Signature.getInstance("DSA");
        if (hasPerfil(cpfDonoAnterior)) {
            Perfil novodono = buscarPerfil(cpfNovoDono);
            for (Documento doc : docs) {
                signDono.initSign(pvKey);
                signDono.update(assinatura);
                if (doc.getPerfil().getCpf().equals(cpfDonoAnterior) && signDono.verify(assinatura)) {
                    Signature signNovoDono = Signature.getInstance("DSA");

                    KeyPair keyP = geradorDeChave().generateKeyPair();

                    doc.setPerfil(novodono);
                    doc.addDonosAnteriores(assinatura);

                    doc.setPbKey(keyP.getPublic());
                    doc.setPvKey(keyP.getPrivate());

                    signNovoDono.initSign(pvKey);
                    signNovoDono.update(doc.getArquivo());
                    byte[] assNovoDono = signNovoDono.sign();
                    doc.setAssinatura(assNovoDono);
                }
            }
        }
    }

    public KeyPairGenerator geradorDeChave() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secRand = new SecureRandom();
        kpg.initialize(512, secRand);

        return kpg;
    }

    public void salvandoDados() throws FileNotFoundException, IOException {
        if (this.perfis != null && this.perfis.isEmpty()) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePerfis));
            output.writeObject(this.perfis);
        }
        if (this.docs != null && this.docs.isEmpty()) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileDocs));
            output.writeObject(this.docs);
        }
        
    }

    public void lendoDados() throws FileNotFoundException, IOException, ClassNotFoundException {

        if (filePerfis.length() > 0) {
            ObjectInputStream inputPerfis = new ObjectInputStream(new FileInputStream(filePerfis));
            if (this.perfis.isEmpty()) {
                this.perfis = (ArrayList<Perfil>) inputPerfis.readObject();
                inputPerfis.close();
            }
        }

        if (fileDocs.length() > 0) {
            ObjectInputStream inputDocs = new ObjectInputStream(new FileInputStream(fileDocs));
            if (this.docs.isEmpty()) {
                this.docs = (ArrayList<Documento>) inputDocs.readObject();
                inputDocs.close();
            }
        }
        /*
        if (filePilotos.length() > 0) {
            ObjectInputStream inputPilotos = new ObjectInputStream(new FileInputStream(filePilotos));
            if (this.pilotos.isEmpty()) {
                this.pilotos = (ArrayList<Piloto>) inputPilotos.readObject();
                inputPilotos.close();
            }
        }

        if (fileJogadores.length() > 0) {
            ObjectInputStream inputJogadores = new ObjectInputStream(new FileInputStream(fileJogadores));
            if (this.jogadores.isEmpty()) {
                this.jogadores = (ArrayList<Jogador>) inputJogadores.readObject();
                inputJogadores.close();
            }
        }
         
    }
*/
    public void criandoArquivos() throws IOException {
        filePerfis = new File("fileperfis.txt");
        if (!filePerfis.exists()) {
            filePerfis = new File("fileperfis.txt");
        }
        
        fileDocs = new File("filedocs.txt");
        if (!fileDocs.exists()) {
            fileDocs = new File("filedocs.txt");
        }
    }


   

}

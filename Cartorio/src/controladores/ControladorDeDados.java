/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import excecoes.DadosIncorretosException;
import excecoes.DocumentoCadastradoException;
import excecoes.LoginRealizadoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import model.Documento;
import model.Perfil;
import util.Hash;

/**
 *
 * @author Teeu Guima
 */
public class ControladorDeDados {

    private ArrayList<Perfil> perfis;
    private ArrayList<Documento> docs;

    private Hash logins;

    File filePerfis = null;
    File fileDocs = null;
    File fileLogins = null;

    public ControladorDeDados() {
        this.perfis = new ArrayList<>();
        this.docs = new ArrayList<>();
        this.logins = new Hash();
    }

    public void cadastrarPerfil(Perfil perfil) throws PerfilCadastradoException {
        if (!perfis.contains(perfil)) {
            perfis.add(perfil);
            throw new PerfilCadastradoException("Perfil cadastrado com sucesso!");
        } else{
            throw new PerfilCadastradoException("Perfil já cadastrado");
        }
    }

    public boolean hasPerfil(String cpf) {
        Iterator iterPerfis = perfis.iterator();
        while (iterPerfis.hasNext()) {
            Perfil perfil = (Perfil) iterPerfis.next();
            if (perfil.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public Perfil buscarPerfil(String cpf) {
        Iterator iterPerfis = perfis.iterator();
        while (iterPerfis.hasNext()) {
            Perfil perfil = (Perfil) iterPerfis.next();
            if (perfil.getCpf().equals(cpf)) {
                return perfil;
            }
        }
        return null;
    }

    public byte[] cadastrarSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte senhaCripto[] = md.digest(senha.getBytes("UTF-8"));
        return senhaCripto;
    }

    public byte[] validarSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte senhaCripto[] = md.digest(senha.getBytes("UTF-8"));
        return senhaCripto;
    }

    public void realizarLogin(String cpf, String senha) throws LoginRealizadoException, PerfilNaoCadastradoException, DadosIncorretosException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            if (Arrays.equals(p.getSenhaCriptografada(), validarSenha(senha))) {
                throw new LoginRealizadoException("Login efetuado com sucesso!");
            } else {
                throw new DadosIncorretosException("Dados invalidos!");
            }
        }
        throw new PerfilNaoCadastradoException("Perfil não encontrado!");
    }

    public void cadastrarDocumento(String cpf, byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DocumentoCadastradoException {
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

        Documento doc = new Documento(buscarPerfil(cpf), pbKey, pvKey, documento, assinatura);
        if(!docs.contains(doc)){
            docs.add(doc);
            throw new DocumentoCadastradoException("Documento cadastrado!");
        }else{
            throw new DocumentoCadastradoException("Documento já cadastrado no sistema!");
        }
        
    }
    
    public ArrayList<Documento> buscarDocumento(String cpf){
        ArrayList<Documento> docsm = new ArrayList<>();
        if(hasPerfil(cpf)){
            Iterator iterDocs = docs.iterator();
            while(iterDocs.hasNext()){
                Documento doc = (Documento) iterDocs.next();
                Perfil perf = buscarPerfil(cpf);
                if(doc.getPerfil().equals(perf)){
                    doc = new Documento(perf.getCpf(), perf.getNome(), perf.getSobrenome(),doc.getArquivo());
                    docsm.add(doc);
                    
                }
            }
            return docsm;
        }
        return null;
    }

    public boolean validarDocumento(String cpf, PublicKey pbKey, byte[] arquivo, byte[] assinatura) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature clienteSign = Signature.getInstance("DSA");
        if (hasPerfil(cpf)) {
            for (Documento doc : docs) {
                clienteSign.initVerify(pbKey);
                clienteSign.update(assinatura);
                if (doc.getPerfil().getCpf().equals(cpf) && clienteSign.verify(assinatura)) {
                    return true;
                }
            }
        }
        return false;
    }

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
        if (this.logins != null && this.logins.isEmpty()) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileLogins));
            output.writeObject(this.logins);
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
         */
    }

    public void criandoArquivos() throws IOException {
        filePerfis = new File("fileperfis.txt");
        if (!filePerfis.exists()) {
            filePerfis = new File("fileperfis.txt");
        }

        fileLogins = new File("fileslogins.txt");
        if (!fileLogins.exists()) {
            fileLogins = new File("filelogins.txt");
        }

        fileDocs = new File("filedocs.txt");
        if (!fileDocs.exists()) {
            fileDocs = new File("filedocs.txt");
        }
    }

}

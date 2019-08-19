/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import excecoes.AutenticidadeDoDocumentoException;
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
    private final String ALGORITHM = "DSA";
    private final String signature = "cartoriodigital";
    File filePerfis;
    File fileDocs;

    public ControladorDeDados() {
        this.perfis = new ArrayList<>();
        this.docs = new ArrayList<>();
        this.filePerfis = null;
        this.fileDocs = null;
    }

    public boolean cadastrarPerfil(Perfil perfil) throws PerfilCadastradoException {
        if (!hasPerfil(perfil.getCpf())) {
            perfis.add(perfil);
            return true;
            // throw new PerfilCadastradoException("Perfil cadastrado com sucesso!");
        } else {
            //  throw new PerfilCadastradoException("Perfil já cadastrado!");
            return false;
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

    public Perfil realizarLogin(String cpf, String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            if (Arrays.equals(p.getSenhaCriptografada(), validarSenha(senha))) {
                return p;
            } else {
                return null;
            }
        }
        return null;
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

    public byte[] assinaturaCartorio() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secRand = new SecureRandom();
        kpg.initialize(512, secRand);

        KeyPair keyP = kpg.generateKeyPair();
        Signature signCartorio = Signature.getInstance(ALGORITHM);
        signCartorio.initSign(keyP.getPrivate());
        signCartorio.update(signature.getBytes());
        return signCartorio.sign();
    }

    public byte[] geradorDeHash(String cpf) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte hashPosse[] = md.digest(cpf.getBytes("UTF-8"));
        return hashPosse;
    }

    public void cadastrarDocumento(String cpf, byte[] documento) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DocumentoCadastradoException, UnsupportedEncodingException, PerfilNaoCadastradoException {

        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            ArrayList<Documento> docss = p.getDocumentos();
            if (!hasDocumento(documento)) {
                Signature sigDono = Signature.getInstance(ALGORITHM);
                sigDono.initSign(p.getPvKey());
                sigDono.update(documento);
                byte[] assinatura = sigDono.sign();
                Documento doc = new Documento(p.getPbKey(), documento, assinatura, p.getHash(), assinaturaCartorio());
                p.getDocumentos().add(doc);
                this.docs.add(doc);
                throw new DocumentoCadastradoException("Documento cadastrado!");
            } else {
                throw new DocumentoCadastradoException("Documento já cadastrado no sistema!");
            }
        } else {
            throw new PerfilNaoCadastradoException("Não há perfil cadastrado com este cpf!");
        }
    }

    public ArrayList<Documento> buscarDocumento(String cpf) throws PerfilNaoCadastradoException {
        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            return p.getDocumentos();

        } else {
            return null;
            // throw new PerfilNaoCadastradoException("Não há perfil cadastrado com este cpf!");
        }
    }

    public void validarDocumento(String cpf, PublicKey pbKey, byte[] arquivo, byte[] assinatura) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, AutenticidadeDoDocumentoException, PerfilNaoCadastradoException {
        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            if (hasDocumento(arquivo)) {
                Signature sign = Signature.getInstance(ALGORITHM);
                Iterator iterDocs = p.getDocumentos().iterator();
                while (iterDocs.hasNext()) {
                    Documento doc = (Documento) iterDocs.next();
                    sign.initVerify(pbKey);
                    sign.update(arquivo);
                    if (sign.verify(assinatura)) {
                        throw new AutenticidadeDoDocumentoException("Documento autêntico!");
                    } else {
                        throw new AutenticidadeDoDocumentoException("Documento não pertencente ao cpf consultado!");
                    }
                }
            }

        } else {
            throw new PerfilNaoCadastradoException("Não há perfil cadastrado com este cpf!");
        }
    }

    public void removerDocumento(String cpfDono, PublicKey pbKey, byte[] assinatura) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (!hasPerfil(cpfDono)) {
            Perfil p = buscarPerfil(cpfDono);
            Signature sign = Signature.getInstance(ALGORITHM);
            Iterator iterDocs = p.getDocumentos().iterator();
            while (iterDocs.hasNext()) {
                Documento doc = (Documento) iterDocs.next();
                sign.initVerify(pbKey);
                sign.update(doc.getArquivo());
                if (sign.verify(assinatura)) {
                    p.getDocumentos().remove(doc);
                }
            }
        }
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
     */
    public KeyPairGenerator geradorDeChave() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secRand = new SecureRandom();
        kpg.initialize(512, secRand);

        return kpg;
    }

    public void salvandoDados() throws FileNotFoundException, IOException {
        if (this.perfis != null || this.perfis.isEmpty()) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePerfis));
            output.writeObject(this.perfis);
        }
        if (this.docs != null || this.docs.isEmpty()) {
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
         */
    }

    public void criandoArquivos() throws IOException, FileNotFoundException, ClassNotFoundException {
        filePerfis = new File("fileperfis.txt");
        if (!filePerfis.exists()) {
            filePerfis = new File("fileperfis.txt");
        }
        /* fileDocs = new File("filedocs.txt");
        if (!fileDocs.exists()) {
            fileDocs = new File("filedocs.txt");
        }
         */
        fileDocs = new File("filedocs.txt");
        if (!fileDocs.exists()) {
            fileDocs = new File("filedocs.txt");
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import excecoes.AutenticidadeDoDocumentoException;
import excecoes.DocumentoCadastradoException;
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
 * Classe responsável pela parte principal do servidor que é armazenar,
 * controlar e realizar operações fundamentais para a aplicação. Cadastrando
 * documentos, perfis, oferecendo serviço de login, busca.
 *
 * @author Mateus Guimarães
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

    /**
     * Método que cria um perfil destinado aos clientes que desejam consultar e
     * cadastrar documentos.
     *
     * @param perfil
     * @return boolean de controle do cadastro.
     * @throws PerfilCadastradoException
     */
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

    /**
     * Método para conferir a existência de um perfil, através do cpf!
     *
     * @param cpf
     * @return boolean, true se existir, false se não há perfil com o cpf no
     * sistema.
     */
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

    /**
     * Método que realiza uma busca na lista de perfis cadastrados, conferindo o
     * cpf recebido. Se há perfil com o cpf pertencente, o perfil é retornado,
     * não há perfil retorno é nulo.
     *
     * @param cpf
     * @return Perfil ou null;
     */
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

    /**
     * Método responsável por gerar uma hash da senha cadastrada pelo cliente,
     * utilizando uma criptografia baseada no algoritmo de criptografia SHA-256.
     *
     * @param senha
     * @return Um array de bytes da senha criptografada.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public byte[] cadastrarSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte senhaCripto[] = md.digest(senha.getBytes("UTF-8"));
        return senhaCripto;
    }

    /**
     * Método responsável por conferir se a senha emitida pelo usuário é
     * idêntica a que o mesmo cadastrou.
     *
     *
     * @param senha
     * @return Um array de bytes com a senha criptografada.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public byte[] validarSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte senhaCripto[] = md.digest(senha.getBytes("UTF-8"));
        return senhaCripto;
    }

    /**
     * Método responsável por realizar login no servidor.
     *
     * @param cpf
     * @param senha
     * @return Objeto do tipo Perfil
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
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
     * Método responsável por gerar uma assinatura baseada em uma string
     * pré-definida. A assinatura é válida para autenticar documentos.
     *
     * @return Array de byte, contendo a assinatura.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
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
     * Método responsável por cadastrar um documento, recebendo o cpf do cliente
     * dono e do arquivo a ser armazenado sobre sua posse. O arquivo é assinado,
     * logo, gera-se um documento com as informações de propriedade.
     *
     * @param cpf
     * @param documento
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws DocumentoCadastradoException
     * @throws UnsupportedEncodingException
     * @throws PerfilNaoCadastradoException
     */
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

    /**
     * Método que retorna uma lista de documentos pertencentes ao cpf
     * verificado.
     *
     * @param cpf
     * @return ArrayList do tipo Documento.
     * @throws PerfilNaoCadastradoException
     */
    public ArrayList<Documento> buscarDocumento(String cpf) throws PerfilNaoCadastradoException {
        if (hasPerfil(cpf)) {
            Perfil p = buscarPerfil(cpf);
            return p.getDocumentos();
        } else {
            return null;
        }
    }

    /**
     * Método responsável por verificar a autenticade de um documento enviado
     * por um cliente. Verificação a partir da chave publica, assinatura e cpf
     * do dono do documento cadastrado nos domínios do cartório.
     *
     * @param cpf
     * @param pbKey
     * @param arquivo
     * @param assinatura
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws AutenticidadeDoDocumentoException
     * @throws PerfilNaoCadastradoException
     */
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

    /**Método responsável por remover um documento através do cpf do proprietário,
     * chave pública e sua assinatura. Retirando assim de sua lista de documentos;
     * 
     * @param cpfDono
     * @param pbKey
     * @param assinatura
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException 
     */
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
    
    /**Método responsável por transferir documento para um novo dono,
     * conferindo a existência deste dono no cartório. A substituição de 
     * posso é realizada em um método interno do documento, que recebe a chave
     * privada, publica, assinatura e a hash do novo dono.
     * 
     * @param cpfDonoAnterior
     * @param cpfNovoDono
     * @param pbKey
     * @param arquivo
     * @param assinatura
     * @return boolean, true se a operação foi concluida com êxito, false se houver erros de existência do perfil ou de
     * falhas na operação.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException 
     */
    public boolean transferirDocumento(String cpfDonoAnterior, String cpfNovoDono, PublicKey pbKey, byte[] arquivo,
            byte[] assinatura) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signDono = Signature.getInstance("DSA");
        if (hasPerfil(cpfDonoAnterior)) {
            Perfil antigoDono = buscarPerfil(cpfDonoAnterior);
            Perfil novoDono = buscarPerfil(cpfNovoDono);
            Iterator iterDoc = antigoDono.getDocumentos().iterator();
            while (iterDoc.hasNext()) {
                Documento doc = (Documento) iterDoc.next();
                signDono.initVerify(pbKey);
                signDono.update(arquivo);
                if (signDono.verify(assinatura)) {
                    removerDocumento(cpfDonoAnterior, pbKey, assinatura);
                    doc.alterarPosse(novoDono.getPvKey(), novoDono.getPbKey(), novoDono.getHash());
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
    /**Método responsável por atualizar o arquivo de persistência de dados!
     * O mesmo atualiza o arquivo com os novos dados inseridos nas estruturas.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
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

    /**Método responsável por ler os dados armazenados nos arquivos e sobrescrever!
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
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
    }

    /**Método responsável por criar os arquivos de textos essenciais
     * para a persistência de dados do servidor (Cartório).
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException 
     */
    public void criandoArquivos() throws IOException, FileNotFoundException, ClassNotFoundException {
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

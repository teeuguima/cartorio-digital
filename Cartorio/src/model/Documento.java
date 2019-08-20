                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import util.ConvertKey;

/**Classe que encapsula as informações de propriedade 
 * dos documentos cadastrados por um cliente.
 *
 * @author Mateus Guimarães
 */
public class Documento implements Serializable {
    private static final long serialVersionUID = 8679233356940587626L;
    private static int idD = 0;
    private int id = 0;
    private ConvertKey convert;
    private byte[] arquivo;
    private PublicKey pbKey;
    private byte[] assinaturaCartorio;
    private byte[] assinatura;
    private byte[] hashDono;
    
    private ArrayList<byte[]> donosAnteriores;

    public Documento(PublicKey pbKey, byte[] arquivo, byte[] assinatura, byte[] hashDono, byte[] assCartorio) {
        this.pbKey = pbKey;
        this.arquivo = arquivo;
        this.assinatura = assinatura;
        this.hashDono = hashDono;
        this.assinaturaCartorio = assCartorio;
        id = idD++;
        this.convert = new ConvertKey();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public PublicKey getPbKey() {
        return pbKey;
    }

    public void setPbKey(PublicKey pbKey) {
        this.pbKey = pbKey;
    }

    public void alterarPosse(PrivateKey pvKey, PublicKey pbKey, byte[] hashNovoDono) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.donosAnteriores.add(hashDono);
        this.pbKey = pbKey;
        
        Signature sign = Signature.getInstance("DSA");
        sign.initSign(pvKey);
        
        sign.update(this.arquivo);
        this.assinatura= sign.sign();
    }

    @Override
    public String toString() {
        return "Documento{" + "Id=" + id + ", Chave Publica=" +convert.converterPublicKey(pbKey) + ", Assinatura=" + new String(assinatura, StandardCharsets.UTF_8)+ ", Arquivo" + '}';
    }
    
    
    
    
}

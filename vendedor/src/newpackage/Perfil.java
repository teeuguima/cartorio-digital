/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import model.Documento;

/**
 *
 * @author Teeu Guima
 */
public class Perfil implements Serializable {
    
    private static final long serialVersionUID = -7401900171994267556L;
    
    private String nome;
    private String sobrenome;
    private String cpf;
    private String rg;
    private String email;
    private String telefone;
    private byte[] senhaCriptografada;
    private byte[] hash;
    
    private PrivateKey pvKey;
    private PublicKey pbKey;
    private ArrayList<Documento> documentos;

    public Perfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, byte[] senha,
        byte[] hash,PrivateKey pvKey, PublicKey pbKey, ArrayList<Documento> docs) {
        this.documentos = docs;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.rg = rg;
        this.email = email;
        this.telefone = telefone;
        this.hash = hash;
        this.senhaCriptografada = senha;
        this.pvKey = pvKey;
        this.pbKey = pbKey;
    }

    public byte[] getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public void setSenhaCriptografada(byte[] senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }

    public PrivateKey getPvKey() {
        return pvKey;
    }

    public void setPvKey(PrivateKey pvKey) {
        this.pvKey = pvKey;
    }

    public PublicKey getPbKey() {
        return pbKey;
    }

    public void setPbKey(PublicKey pbKey) {
        this.pbKey = pbKey;
    }

    public ArrayList<Documento> getDocumentos() {
        return documentos;
    }
/*
    public static void setDocumentos(ArrayList<Documento> documentos) {
        this.documentos = documentos;
    }
*/
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void addDocumento(Documento doc) {
        this.documentos.add(doc);
    }

}

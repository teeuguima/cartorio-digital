/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author Teeu Guima
 */
public class Perfil implements Serializable {

    private String nome;
    private String sobrenome;
    private String cpf;
    
    private byte[] hash;
    
    private PrivateKey pvKey;
    private PublicKey pbKey;
    private ArrayList<Documento> documentos;

    public Perfil(String nome, String sobrenome, String cpf, PrivateKey pvKey, PublicKey pbKey) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.pvKey = pvKey;
        this.pbKey = pbKey;
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

    public void setDocumentos(ArrayList<Documento> documentos) {
        this.documentos = documentos;
    }

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

    public void addDocumento(Documento doc) {
        this.documentos.add(doc);
    }

}

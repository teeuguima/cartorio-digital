/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author Teeu Guima
 */
public class Documento implements Serializable {
    private String cpf;
    private static int idD=0;
    private int id =0;
    private PublicKey pbKey;
    private byte[] arquivo;
    private byte[] assinatura;
    private byte[] hashDono;
    
    private ArrayList<byte[]> donosAnteriores;
    
    public Documento(String cpf, PublicKey pbKey, byte[] arquivo, byte[] assinatura, byte[] hashDono) {
        this.cpf = cpf;
        this.pbKey = pbKey;
        this.arquivo = arquivo;
        this.assinatura = assinatura;
        this.id= idD++;
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

    public byte[] getHashDono() {
        return hashDono;
    }

    public void setHashDono(byte[] hashDono) {
        this.hashDono = hashDono;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void addDonosAnteriores(byte[] assinatura){
        donosAnteriores.add(assinatura);
    }
    
    
    
    
}

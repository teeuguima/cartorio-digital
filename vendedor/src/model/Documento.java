/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author Teeu Guima
 */
public class Documento {
    private int id=0;
    private Perfil perfil;
    private byte[] arquivo;
    private PublicKey pbKey;
    private PrivateKey pvKey;
    private byte[] assinatura;
    
    private ArrayList<byte[]> donosAnteriores;
    
    public Documento(Perfil perfil, PublicKey pbKey, PrivateKey pvKey, byte[] arquivo, byte[] assinatura) {
        this.perfil = perfil;
        this.pbKey = pbKey;
        this.pvKey = pvKey;
        this.arquivo = arquivo;
        this.assinatura = assinatura;
        this.id= id+1;
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
    
    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
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

    public PrivateKey getPvKey() {
        return pvKey;
    }

    public void setPvKey(PrivateKey pvKey) {
        this.pvKey = pvKey;
    }
    
    public void addDonosAnteriores(byte[] assinatura){
        donosAnteriores.add(assinatura);
    }
    
    
    
    
}

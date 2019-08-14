/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Teeu Guima
 */
public class Perfil implements Serializable {

    private String nome;
    private String sobrenome;
    private String cpf;
    private String rg;
    private String email;
    private String telefone;
    private byte[] senhaCriptografada;
    
   // private ArrayList<byte[]> documentos;
    
    public Perfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, byte[] senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.rg = rg;
        this.email = email;
        this.senhaCriptografada = senha;
        this.telefone = telefone;
    }

    public byte[] getSenhaCriptografada(){
        return senhaCriptografada;
    }
    
    public void setSenhaCriptografada(byte[] senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
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
    
    

}

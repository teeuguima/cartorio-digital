package controladores;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import model.Perfil;

public class ControladorFactory {

    /**
     *Factory é um projeto de sistema que "fabrica" objeto e devolve suas referências 
     */
    public ControladorFactory() {} 
    
    public Perfil factoryPerfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, byte[] senha,byte[] hash, PrivateKey pvKey, PublicKey pbKey){
        return new Perfil(nome, sobrenome, cpf, rg, email, telefone, senha,hash,pvKey,pbKey);
    }
    
    
}

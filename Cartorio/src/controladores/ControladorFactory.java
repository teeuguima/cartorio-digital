package controladores;

import java.util.List;
import model.Perfil;

public class ControladorFactory {

    /**
     *Factory é um projeto de sistema que "fabrica" objeto e devolve suas referências 
     */
    public ControladorFactory() {} 
    
    public Perfil factoryPerfil(String nome, String sobrenome, String cpf, String rg, String email, String telefone, String senha){
        return new Perfil(nome, sobrenome, cpf, rg, email, telefone, senha);
    }
    
    
}

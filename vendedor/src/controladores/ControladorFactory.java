package controladores;

import model.Perfil;

public class ControladorFactory {

    /**
     *Factory é um projeto de sistema que "fabrica" objeto e devolve suas referências 
     */
    public ControladorFactory() {} 
    
    public Perfil factoryPerfil(String nome, String sobrenome, String cpf){
        return new Perfil(nome, sobrenome, cpf);
    }
    
    
}

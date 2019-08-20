package controladores;

import java.security.PrivateKey;
import java.security.PublicKey;
import model.Perfil;

/**Factory é um projeto de sistema que "fabrica" objeto e devolve suas
 * referências
 *
 * @author Mateus Guimarães e Juliana Aragão (Desenvolvida durante o 1º
 * Problema)
 */
public class ControladorFactory {

    /**
     *Factory é um projeto de sistema que "fabrica" objeto e devolve suas referências 
     */
    public ControladorFactory() {} 
    
    public Perfil factoryPerfil(String nome, String sobrenome, String cpf,PrivateKey pvKey, PublicKey pbKey){
        return new Perfil(nome, sobrenome, cpf, pvKey, pbKey);
    }
    
    
}

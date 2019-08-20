package controladores;

import comunicacao.Mensagem;

/**Classe para controlar as mensagens de um cliente!
 * 
 * @author Mateus Guimarães e Juliana Aragão (Desenvolvida durante o 1º Problema)
 */
public class ControladorDeMensagens {

    private Mensagem mensagemPessoaFis;

    public ControladorDeMensagens() {
        mensagemPessoaFis = new Mensagem();
    }

    public void novaMensagem(byte[] bytes) {
        mensagemPessoaFis.setBytes(bytes);
        mensagemPessoaFis.setHasMensagemToTrue();

    }

    public Mensagem getMensagem() {
        return this.mensagemPessoaFis;
    }

}

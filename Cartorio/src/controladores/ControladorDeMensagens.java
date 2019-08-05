package controladores;

import comunicacao.Mensagem;

public class ControladorDeMensagens {

    private Mensagem mensagemPessoaFis;

    public ControladorDeMensagens() {
        mensagemPessoaFis = new Mensagem();
    }

    public void novaMensagem(/*byte[] bytes*/ String info) {
        
        //mensagemPessoaFis.setBytes(bytes);
        mensagemPessoaFis.setMessage(info);
        mensagemPessoaFis.setHasMensagemToTrue();

    }

    public Mensagem getMensagem() {
        return this.mensagemPessoaFis;
    }

}

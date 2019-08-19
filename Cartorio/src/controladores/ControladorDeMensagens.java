package controladores;

import comunicacao.Mensagem;

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

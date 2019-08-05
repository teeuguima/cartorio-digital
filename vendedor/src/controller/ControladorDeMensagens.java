package controller;

import protocolo.Mensagem;

public class ControladorDeMensagens {

    private Mensagem mensagemPessoaFis;

    public ControladorDeMensagens() {
        mensagemPessoaFis = new Mensagem();
    }

    public void novaMensagem(byte[] bytes) {

        mensagemPessoaFis.setBytes(bytes);
        mensagemPessoaFis.setHasMensagemToTrue();

    }

    public void novaMensagem(String message) {
        System.out.println("Passou!");
        mensagemPessoaFis.setMessage(message);
        System.out.println("Setado");
        mensagemPessoaFis.setHasMensagemToTrue();
        System.out.println("Setado True");

    }

    public Mensagem getMensagem() {
        return this.mensagemPessoaFis;

    }

}

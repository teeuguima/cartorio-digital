package protocolo;

/**Classe responsável por aplicar um protocolo
 * para encapsular as mensagens de entrada e 
 * saída do servidor.
 * 
 * @author Mateus Guimarães
 */
public class Mensagem {

    private boolean has;
    private byte[] bytes;
    private String message;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatusConexao(boolean status) {
        this.status = status;
    }

    public Mensagem() {
        has = false;
        status = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setHasMensagemToTrue() {
        has = true;
    }

    public boolean hasMensagem() {
        return has;
    }

    public void enviouMensagem() {
        has = false;
    }
}

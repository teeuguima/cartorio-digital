package comunicacao;

/**Classe responsável por aplicar um protocolo
 * para encapsular as mensagens de entrada e 
 * saída do servidor.
 * 
 * @author Mateus Guimarães
 */
public class Mensagem {
    
    private boolean has;
    private String message;
    private byte[] bytes;

    public Mensagem() {     
        has = false;
    }

    public boolean isHas() {
        return has;
    }

    public void setHas(boolean has) {
        this.has = has;
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
    
    public void setHasMensagemToTrue(){
        has = true;
    }

    public boolean hasMensagem(){
        return has;
    }
    
    public void enviouMensagem(){
        has = false;
    }
    
}

package comunicacao;

import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import excecoes.SocketTratadoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadConections extends Thread {

    ConectionIO pessoafisica;

    public ThreadConections(ConectionIO pessoafisica) {
        this.pessoafisica = pessoafisica;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pessoafisica.tratar();

            } catch (IOException | ClassNotFoundException | PerfilCadastradoException | InvalidKeySpecException
                    | InterruptedException | PerfilNaoCadastradoException | SocketTratadoException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
                //System.out.println("Erro: " + ex);
                interrupt();
            }
        }

    }
}

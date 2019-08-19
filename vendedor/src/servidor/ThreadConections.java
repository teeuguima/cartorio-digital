package servidor;

import excecoes.SocketTratadoException;
import java.io.IOException;

public class ThreadConections extends Thread {

    ConectionIO pessoafisica;

    public ThreadConections(ConectionIO pessoafisica) {
        this.pessoafisica = pessoafisica;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (true) {
            try {
                //System.out.println("Entrou!");
                pessoafisica.tratar();
                
            } catch (IOException | InterruptedException | SocketTratadoException | ClassNotFoundException ex) {
              interrupt();
            } 
        }
    }
}

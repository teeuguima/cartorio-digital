package servidor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadConections extends Thread {

    ConectionIO pessoafisica;

    public ThreadConections(ConectionIO pessoafisica) {
        this.pessoafisica = pessoafisica;
    }

    @Override
    public void run() {
        boolean flag = true;
        try {
            //System.out.println("Entrou!");
            pessoafisica.tratar();

        } catch (IOException ex) {
            System.out.println("Erro: " + ex);
            interrupt();
            flag = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadConections.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadConections.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

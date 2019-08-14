package comunicacao;

import java.io.IOException;

public class ThreadConections extends Thread {

    ConectionIO pessoafisica;

    public ThreadConections(ConectionIO pessoafisica) {
        this.pessoafisica = pessoafisica;
    }

    @Override
    public void run() {
        boolean flag = true;
       // while (flag) {
            try {
                //System.out.println("Entrou!");
                pessoafisica.tratar();

            } catch (IOException | ClassNotFoundException ex) {
               //System.out.println("Erro: " + ex);
                interrupt();
                flag = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
       // }

    }

}

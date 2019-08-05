package controller;


import facade.ClienteServidorFacade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * O controlador de dados controla os dados cadastráveis do sistema. Essa classe
 * na segunda versão ganhará um banco de dados.
 */
public class ControladorDeDados {



    private File fileCarros = null;
    private File fileEquipes = null;
    private File filePilotos = null;
    private File fileJogadores = null;
    
    private static ControladorDeDados dados;
    /**
     * Instancia das listas encadeadas
     */
    public ControladorDeDados() {
        

    }
    
     public static synchronized ControladorDeDados getInstance() {
        return (dados == null) ? dados = new ControladorDeDados(): dados;
    }

    

    public void salvandoDados() throws FileNotFoundException, IOException {
        /*
        if (this.carros != null && this.carros.size() > 0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileCarros));
            output.writeObject(this.carros);
        }

        if (this.equipes != null && this.equipes.size() > 0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileEquipes));
            output.writeObject(this.equipes);
        }

        if (this.pilotos != null && this.pilotos.size() > 0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePilotos));
            output.writeObject(this.pilotos);
        }

        if (this.jogadores != null && this.jogadores.size() > 0) {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileJogadores));
            output.writeObject(this.jogadores);
        }
         */
    }

    public void lendoDados() throws FileNotFoundException, IOException, ClassNotFoundException {
        /*
        if (fileCarros.length() > 0) {
            ObjectInputStream inputCarros = new ObjectInputStream(new FileInputStream(fileCarros));
            if (this.carros.isEmpty()) {
                this.carros = (ArrayList<Carro>) inputCarros.readObject();
                inputCarros.close();
            }
        }

        if (fileEquipes.length() > 0) {
            ObjectInputStream inputEquipes = new ObjectInputStream(new FileInputStream(fileEquipes));
            if (this.equipes.isEmpty()) {
                this.equipes = (ArrayList<Equipe>) inputEquipes.readObject();
                inputEquipes.close();
            }
        }

        if (filePilotos.length() > 0) {
            ObjectInputStream inputPilotos = new ObjectInputStream(new FileInputStream(filePilotos));
            if (this.pilotos.isEmpty()) {
                this.pilotos = (ArrayList<Piloto>) inputPilotos.readObject();
                inputPilotos.close();
            }
        }

        if (fileJogadores.length() > 0) {
            ObjectInputStream inputJogadores = new ObjectInputStream(new FileInputStream(fileJogadores));
            if (this.jogadores.isEmpty()) {
                this.jogadores = (ArrayList<Jogador>) inputJogadores.readObject();
                inputJogadores.close();
            }
        }
         */
    }

    public void criandoArquivos() throws IOException {
        fileCarros = new File("filecarros.txt");
        if (!fileCarros.exists()) {
            fileCarros = new File("filecarros.txt");
        }

        fileEquipes = new File("fileequipes.txt");
        if (!fileEquipes.exists()) {
            fileEquipes = new File("fileequipes.txt");
        }

        filePilotos = new File("filepilotos.txt");
        if (!filePilotos.exists()) {
            filePilotos = new File("filepilotos.txt");
        }

        fileJogadores = new File("filejogadores.txt");
        if (fileJogadores.exists()) {
            fileJogadores = new File("filejogadores.txt");
        }
    }

}

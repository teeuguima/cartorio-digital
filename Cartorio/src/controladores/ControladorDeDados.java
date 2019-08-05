package controladores;

import excecoes.DadosIncorretosException;
import excecoes.LoginRealizadoException;
import excecoes.PerfilCadastradoException;
import excecoes.PerfilNaoCadastradoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import model.Login;
import model.Perfil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * O controlador de dados controla os dados cadastráveis do sistema. Essa classe
 * na segunda versão ganhará um banco de dados.
 */
public class ControladorDeDados {

    private LinkedList<Perfil> perfis;
    private LinkedList<Perfil> logins;

    private File fileCarros = null;
    private File fileEquipes = null;
    private File filePilotos = null;
    private File fileJogadores = null;

    /**
     * Instancia das listas encadeadas
     */
    public ControladorDeDados() {
        perfis = new LinkedList<>();
        logins = new LinkedList();

    }

    public void adicionarPerfil(Perfil perfil) throws PerfilCadastradoException {
        if (!perfis.contains(perfil)) {
            perfis.add(perfil);
        } else {
            throw new PerfilCadastradoException("Perfil já cadastrado no sistema!");
        }
    }
    
    
    public void realizaLogin(String email, String senha) throws LoginRealizadoException, DadosIncorretosException, PerfilNaoCadastradoException  {
        Iterator<Perfil> iter_perfis = perfis.iterator();
        while (iter_perfis.hasNext()) {
            Perfil p = (Perfil) iter_perfis.next();
            if (p.getEmail().equals(email) && p.getSenha().equals(senha)) {
                logins.add(p);
                throw new LoginRealizadoException("Login efetuado com sucesso!");
            }else if(!p.getEmail().equals(email)){
                throw new PerfilNaoCadastradoException("Não há perfil cadastrado com este email!");
            }
            else{
                throw new DadosIncorretosException("Os dados inseridos estão incorretos!");
            }
        }
    }
    
    public void realizaLogout(Perfil perfil){
        Iterator<Perfil> iter_perfis = perfis.iterator();
        while (iter_perfis.hasNext()) {
            Perfil p = (Perfil) iter_perfis.next();
            if (p.getEmail().equals(perfil.getEmail()) && p.getSenha().equals(perfil.getSenha())) {
                logins.remove(p);
            }
        }
    }

    public Perfil hasPerfil(String email) {
        Iterator<Perfil> iter_perfis = perfis.iterator();
        while (iter_perfis.hasNext()) {
            Perfil p = (Perfil) iter_perfis.next();
            if (p.getEmail().equals(email)) {
                return p;
            }
        }
        return null;
    }

    public void buscaPerfil(Perfil perfil) {

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

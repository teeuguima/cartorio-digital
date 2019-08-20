/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cartorio;

import comunicacao.ConectionIO;
import comunicacao.Conexao;
import comunicacao.ThreadConections;
import facade.ServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

/**Classe principal para execução dos serviços de
 * cartório.
 *
 * @author Mateus Guimarães
 */
public class Cartorio {
    private static ServidorFacade facade;
    private static ThreadConections tcIO;

    public Cartorio() throws IOException, FileNotFoundException, ClassNotFoundException {
        facade = ServidorFacade.getInstance();
    }
 
    public ServidorFacade getFacade(){
        return facade;
    }
    
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
        
        Cartorio cartorio = new Cartorio();
        cartorio.getFacade().criandoArquivos();
        cartorio.getFacade().lerDados();
        Conexao conexao = new Conexao(facade); //Classe responsável pela criação de conexões
        conexao.criarServerSocket();
        try {
            while (true) {
                /**Espera a conexão de algum cliente, se houver conexão estabelecida
                 * uma thread para tratar a conexão é lançada e executada!
                 * 
                 */
                Socket socket = conexao.esperandoConexao();
                tcIO = new ThreadConections(new ConectionIO(socket, facade));
                new Thread(tcIO).start();
                
            }
        } catch (IOException | NullPointerException ex) {
        
        }
    }

    

}

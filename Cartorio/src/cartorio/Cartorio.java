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

/**
 *
 * @author Teeu Guima
 */
public class Cartorio {

    private static ServidorFacade facade;
    private static ThreadConections tcIO;

    public Cartorio() throws IOException, FileNotFoundException, ClassNotFoundException {
        facade = ServidorFacade.getInstance();
    }
   /* 
    public ServidorFacade getFacade(){
        return facade;
    }
    */
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
        //Cartorio cartorio = new Cartorio();
        //  ServerSocket serverSock = new ServerSocket(5555);
        //  cartorio.conexao.criarServerSocket();
        Cartorio cartorio = new Cartorio();
        facade.criandoArquivos();
        facade.lerDados();
        Conexao conexao = new Conexao(facade);
        conexao.criarServerSocket();
        try {
            while (true) {
                Socket socket = conexao.esperandoConexao();
                System.out.println(socket.getInputStream().available());
                tcIO = new ThreadConections(new ConectionIO(socket, facade));
                //tcIO = new ThreadConections(facade.getConectionIOADM(), facade.getConectionIOSensor());
                System.out.println("Tudo Certo!");
                new Thread(tcIO).start();
                facade.armazenarDados();
            }
        } catch (IOException | NullPointerException ex) {
            // ex.printStackTrace();
            // System.out.println("Erro" + ex);
        }
    }

    

}

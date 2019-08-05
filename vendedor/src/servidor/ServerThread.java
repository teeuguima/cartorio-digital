/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import facade.ClienteServidorFacade;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Teeu Guima
 */
public class ServerThread extends Thread{
    private ServerSocket server;
    private Set<TratamentoServidor> serverThread = new HashSet<TratamentoServidor>();
    private ClienteServidorFacade facade;
    
    public ServerThread(int porta) throws IOException{
        server = new ServerSocket(porta);
        this.facade = new ClienteServidorFacade();
    }
    
    @Override
    public void run(){
        try {
            while(true){
                TratamentoServidor serverThreadThread = new TratamentoServidor(server.accept(), facade, this);
                serverThread.add(serverThreadThread);
                serverThreadThread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public Set<TratamentoServidor> getServerThreadThread(){
        return serverThread;
    }
    
    public void sendMessage(String messagem){
        try {
            serverThread.forEach(t-> t.getPrintWriter().println(messagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Set<TratamentoServidor> getServerThreadThreads(){
        return serverThread;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author Teeu Guima
 */
public class PeerThread extends Thread {
    private BufferedReader bufferRead;
    
    public PeerThread(Socket socket) throws IOException{
        bufferRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
    }
    
    @Override
    public void run(){
        boolean flag = true;
        while(flag){
            try {
                String info = bufferRead.readLine();
                JSONObject json = new JSONObject(info);
                if(json.has("username")){
                    System.out.println("["+ json.getString("username")+"]:"+ json.getString("message"));
                }
            } catch (IOException e) {
                flag = false;
                interrupt();
            }
        }
    }
}

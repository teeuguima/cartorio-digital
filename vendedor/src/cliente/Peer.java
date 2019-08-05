/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import servidor.ServerThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author Teeu Guima
 */
public class Peer {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Entre com o nome de usuário e porta para este cliente:");
        String[] values = bufferReader.readLine().split(" ");
        /*       
       ServerThread serverTh = new ServerThread(porta);
        serverTh.start();
        new Peer().updateListenToPeers(bufferReader, values[0], serverTh);
         */
    }

    public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws IOException {
        System.out.println("entre (espaço separado) hostname:porta");
        System.out.println("para receber messagens de(s para pular)");

        String input = bufferedReader.readLine();
        String[] values = input.split(" ");

        if (!input.equals("s")) {
            for (int i = 0; i < values.length; i++) {
                String[] endereco = values[i].split(":");
                Socket socket = null;
                try {
                    socket = new Socket(endereco[0], Integer.valueOf(endereco[1]));
                    new PeerThread(socket).start();
                } catch (IOException e) {
                    if (socket != null) {
                        socket.close();
                    } else {
                        System.out.println("Entrada Invalida, tente novamente");
                    }
                }
            }
        }
        communicate(bufferedReader, username, serverThread);
    }

    public void communicate(BufferedReader buffer, String nickname, ServerThread serverTh) {
        try {
            System.out.println("> Agora você pode se comunicar(e para sair, c para escolher");
            BufferedReader buffered = new BufferedReader(new InputStreamReader(System.in));
            boolean flag = true;

            String input = buffer.readLine();
            if (input.equals("e")) {
                flag = false;
            } else if (input.equals("c")) {
                while (flag) {
                    String mensagem;
                    System.out.println("Escreva Algo ou Digite 'S' Para Sair!");
                    mensagem = buffered.readLine();
                    if (mensagem.equals("S")) {
                        break;
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("username", nickname);
                        json.put("message", mensagem);
                        serverTh.sendMessage(json.toString());
                    }

                }
            }
        } catch (IOException e) {
        }
    }

}

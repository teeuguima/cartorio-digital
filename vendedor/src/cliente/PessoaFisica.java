/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import facade.ClienteServidorFacade;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import util.Console;

/**
 *
 * @author Teeu Guima
 */
public class PessoaFisica {

    private static Conexao conexao;
    private ClienteServidorFacade facade;
    private String opc = "N";

    public PessoaFisica() {
        facade = new ClienteServidorFacade();
        conexao = new Conexao(facade);
    }

    private int voltarMenu(String opc) {
        this.opc = opc;
        if (opc.equals("S") | opc.equals("s")) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Getter para obter valor da string que captura a opção de retorno ao menu.
     *
     * @return String da opção designada.
     */
    public String getOpc() {
        return opc;
    }

    /**
     * Para criação do menu principal pro usuário.
     *
     * @return inteiro com a opção desejada.
     * @throws IOException
     */
    public int menuPrincipal() throws IOException {
        int opc = 0;
        try {
            System.out.println("Menu Internet dos Brinquedos");
            System.out.println(
                    "1- Cadastrar Documento no Cartório\n"
                    + "2- Contatar Vendedor\n"
                    + "3- Autenticar Documento");
            opc = Console.readInt();

        } catch (NumberFormatException e) {
            System.out.println("Digite só números!");
            System.out.println("");
            menuPrincipal();
        }
        return opc;
    }

    public byte[] convertToByte(String dados) {
        System.out.println(dados);
        return dados.getBytes(StandardCharsets.UTF_8);
    }

    public String convertToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void conectarAoCartorio() throws IOException {
        /*System.out.println("Para se conectar ao cartório, informe o endereço ip e a porta deste cartório!");
        System.out.println("[Endereço]:");
        String endereco = Console.readString();
        System.out.println("[Porta]:");
        int porta = Console.readInt();
         */
        conexao.conectarComCliente("localhost", 5555);
    }

    public int cadastroNoCartorio() throws IOException, InterruptedException {
        String op;
        /*
        System.out.println("É necessário realizar um cadastro, para ter acesso!");

        System.out.println("Informe seu nome:");
        String nome = Console.readString();
            
        System.out.println("Informe seu sobrenome:");
        String sobrenome = Console.readString();

        System.out.println("Insira o número do seu cpf:");
        String cpf = Console.readString();

        System.out.println("Informe o número do RG:");
        String rg = Console.readString();

        System.out.println("Informe um email e uma senha valida");
        System.out.println("Email:");
        String email = Console.readString();
        System.out.println("Senha:");
        String senha = Console.readString();
        System.out.println("Informe seu Telefone:");
        String telefone = Console.readString();
         */
        JSONObject dados = new JSONObject();
        dados.put("nome", "Mateus");
        dados.put("sobrenome", "Guimarães");
        dados.put("cpf", "01212");
        dados.put("rg", "36212");
        dados.put("email", "mgda@gmail.com");
        dados.put("telefone", "73612121");
        dados.put("senha", "haikast");
        dados.put("command", "CadastroPerfil");

        System.out.println(dados.toString());
        //convertToByte(dados);
        byte[] bytes = convertToByte(dados.toString());
        System.out.println(bytes.length);
        facade.novaMensagem(convertToByte(dados.toString()));

//        System.out.println(facade.getRespostaCartorio().getString("status"));
        do {
            System.out.println("Deseja cadastrar novamente? S/N");
            op = Console.readString();
        } while (!op.matches("[^\\d]+"));
        if (op.equals("S") || op.equals("s")) {
            return 1;
        } else {
            return 0;
        }

    }

    public int realizarLogin() throws IOException, InterruptedException {
        String op;

        conectarAoCartorio();

        System.out.println("Já cadastrou seu perfil? S/N");
        String resposta = Console.readString();
        if (resposta.equals("S") || resposta.equals("s")) {
            System.out.println("Realize login para ter acesso ao sistema");
            System.out.println("Informe seu email");
            String email = Console.readString();
            System.out.println("Insira a senha");
            String senha = Console.readString();

            JSONObject dados = new JSONObject();
            dados.put("email", email);
            dados.put("senha", senha);
            dados.put("command", "Login");

            facade.novaMensagem(convertToByte(dados.toString()));
        } else if (resposta.equals("N") || resposta.equals("n")) {
            cadastroNoCartorio();
        }

        do {
            System.out.println("Deseja cadastrar novamente? S/N");
            op = Console.readString();
        } while (!op.matches("[^\\d]+"));
        if (op.equals("S") || op.equals("s")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void inicializaServidor() throws IOException {
        conexao.criarServidor();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        PessoaFisica pessoa = new PessoaFisica();
        int repeat = 0;
        try {
            pessoa.inicializaServidor();
            pessoa.realizarLogin();
            do {

                int controle = pessoa.menuPrincipal();
                switch (controle) {

                    case 1:
                        do {
                            repeat = pessoa.realizarLogin();
                        } while (repeat == 1);
                        break;

                    case 2:
                        do {
                            // repeat = admin.cadastroJogadores();
                        } while (repeat == 1);
                        break;
                    case 3:
                        // admin.iniciaPartida();
                        break;
                }
            } while (repeat == 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

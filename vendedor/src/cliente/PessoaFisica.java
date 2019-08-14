/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import comunicacao.Conexao;
import facade.ClienteServidorFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Perfil;
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
    private Perfil perfil;

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
                    + "2- Buscar um Documento\n"
                    + "3- Contatar um Vendedor"
                    + "4- Verificar um Documento");
            opc = Console.readInt();

        } catch (NumberFormatException e) {
            System.out.println("Digite só números!");
            System.out.println("");
            menuPrincipal();
        }
        return opc;
    }

    public byte[] convertToByte(String dados) {
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

    public void conectarAoCliente() throws IOException {
        System.out.println("Informe o endereço ip e porta de quem irá se conectar");
        System.out.println("[Endereço]:");
        String endereco = Console.readString();
        System.out.println("[Porta]:");
        int porta = Console.readInt();

        conexao.conectarComCliente(endereco, porta);
    }

    public void cadastrarDocumento() throws FileNotFoundException, IOException, InterruptedException {
        String opc = null;
        do {
            JFileChooser filee = new JFileChooser();
            FileNameExtensionFilter fileNameExt = new FileNameExtensionFilter(null,"txt");
            filee.setFileFilter(fileNameExt);
            filee.setDialogTitle("Selecione o Documento");

            int status = filee.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = new File(filee.getSelectedFile().getAbsolutePath());
                byte[] arqvConvrt = new byte[(int) file.length()];

                FileInputStream inFile = new FileInputStream(file);

                try {
                    inFile.read(arqvConvrt, 0, (int) file.length());
                    String byteString = new String(arqvConvrt, StandardCharsets.UTF_8);
                    JSONObject json = new JSONObject();

                    System.out.println("Informe o seu CPF para confirmação de segurança");
                    System.out.println("[CPF]:");
                    String cpf = Console.readString();

                    json.put("command", "CadastrarDocumento");
                    json.put("cpf", cpf);
                    json.put("documento", byteString);
                    facade.novaMensagem(convertToByte(json.toString()));
                    String resposta = facade.getRespostaCartorio().getString("status");
                    
                    
                } catch (IOException e) {
                    System.out.println("Erro: " + e);
                }
            }
            System.out.println("Deseja cadastrar outro documento? S/N");
            opc = Console.readString();
        } while (opc.compareTo("S") == 0 || opc.compareTo("s") == 0);
        //facade.fecharConexao();
    }
    
    public void buscarDocumento() throws IOException{
        System.out.println("Informe o Nº do CPF para obter o(s) documento(s)!");
        String cpf = Console.readString();
        
        
    }

    public void cadastroNoCartorio() throws IOException, InterruptedException {
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

        perfil = new Perfil("Mateus", "Guimarães", "01212");
        //System.out.println(dados.toString());
        //convertToByte(dados);
        //System.out.println(bytes.length);
        facade.novaMensagem(convertToByte(dados.toString()));
        System.out.println(facade.getRespostaCartorio().getString("status"));
        if (facade.getRespostaCartorio().getString("status").compareTo("Perfil cadastrado com sucesso!") == 0) {
            System.out.println("Utilize seu CPF e senha para login!");
            loginNoCartorio();
        }
//        System.out.println(facade.getRespostaCartorio().getString("status"));
    }

    public void loginNoCartorio() throws IOException, InterruptedException {
        System.out.println("Para realizar login:");
        System.out.println("Informe seu CPF");
        System.out.println("[CPF]");
        String cpf = Console.readString();
        System.out.println("Insira a senha cadastrada");
        String senha = Console.readString();

        JSONObject dados = new JSONObject();
        dados.put("cpf", cpf);
        dados.put("senha", senha);
        dados.put("command", "Login");

        facade.novaMensagem(convertToByte(dados.toString()));
        String status = facade.getRespostaCartorio().getString("status");
        if (status.compareTo("Login efetuado com sucesso!") == 0) {
            System.out.println("Login efetuado com sucesso!");
        } else {
            System.out.println(status);
        }
    }

    public void realizarLogin() throws IOException, InterruptedException {
        conectarAoCartorio();

        System.out.println("Possui perfil cadastrado no cartório? S/N");
        String resposta = Console.readString();
        if (resposta.equals("S") | resposta.equals("s")) {
            loginNoCartorio();
        } else {
            cadastroNoCartorio();
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
            pessoa.realizarLogin(); //Conecta ao cartório para realizar login!
            do {
                System.out.println("\nO que desejas realizar?\n");
                int controle = pessoa.menuPrincipal();
                switch (controle) {
                    case 1:
                        do {
                            pessoa.cadastrarDocumento();
                        } while (repeat == 1);
                        break;

                    case 2:
                        //   do {
                        pessoa.conectarAoCliente();
                        //   } while (repeat == 1);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import comunicacao.Conexao;
import controladores.ControladorDeMensagens;
import facade.ClienteServidorFacade;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import model.Perfil;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Console;
import util.ConvertKey;
import util.GerenteArquivos;

/**Classe principal, responsável pela execução e entrada de dados, exibição das sáidas 
 * e etc...
 *
 * @author Mateus Guimarães
 */
public class Cliente {

    private static Conexao conexao;
    private ClienteServidorFacade facade;
    private String opc = "N";
    private Perfil perfil;
    private ConvertKey convert;
    private GerenteArquivos garqs;

    public Cliente() throws IOException, FileNotFoundException, ClassNotFoundException {
        //facade = new ClienteServidorFacade();
        facade = new ClienteServidorFacade(new ControladorDeMensagens());
        conexao = new Conexao(facade);
        convert = new ConvertKey();
        garqs = new GerenteArquivos();
    }

    /**
     * Método para controle da opção de retorno ao menu!
     *
     * @param opc
     * @return int
     */
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
            System.out.println("*****************************");
            System.out.println("[ Menu Cartório Digital ]");
            System.out.println("*****************************");
            System.out.println(
                    "1- Cadastrar Documento no Cartório\n"
                    + "2- Buscar um Documento\n"
                    + "3- Contatar um Vendedor\n"
                    + "4- Verificar um Documento");
            opc = Console.readInt();

        } catch (NumberFormatException e) {
            System.out.println("Digite só números!");
            System.out.println("");
            menuPrincipal();
        }
        return opc;
    }

    /**
     * Opções para serem realizadas ao contatar um cliente!
     *
     * @return int
     * @throws IOException
     * @throws InterruptedException
     */
    public int menuCliente() throws IOException, InterruptedException {
        int opc = 0;
        try {
            System.out.println("*****************************");
            System.out.println("[ Menu Cartório Digital ]");
            System.out.println("*****************************");
            System.out.println(
                    "1- Solicitar Arquivo Para Autenticar\n"
                    + "2- Solicitar Transferência\n");
            opc = Console.readInt();

        } catch (NumberFormatException e) {
            System.out.println("Digite só números!");
            System.out.println("");
            menuPrincipal();
        }

        switch (opc) {
            case 1:
                solicitarUmDocumento();
                break;
            case 2:
                solicitarTransferencia();
                break;
        }
        return opc;
    }

    /**
     * Método para converter uma String em array de byte
     *
     * @param dados
     * @return array de byte.
     */
    public byte[] convertToByte(String dados) {
        return dados.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Método para converter um array de byte em String.
     *
     * @param bytes
     * @return String
     */
    public String convertToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Método para realizar uma conexão a um servidor, especificamente um
     * cartório que irá responder as requisições deste cliente.
     *
     * @throws IOException
     */
    public void conectarAoCartorio() throws IOException {
        System.out.println("*****************************");
        System.out.println("Para se conectar ao cartório, informe o endereço ip e a porta deste cartório!");
        System.out.println("[Endereço]:");
        String endereco = Console.readString();
        System.out.println("[Porta]:");
        int porta = Console.readInt();

        conexao.conectarComCliente(endereco, porta);
    }

    /**
     * Método responsável pela conexão com um cliente/servidor que tem como
     * papel de enviar informações sobre documentos solicitados.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void conectarComUmCliente() throws IOException, InterruptedException {
        System.out.println("Informe o endereço ip e porta de quem irá se conectar");
        System.out.println("[Endereço]:");
        String endereco = Console.readString();
        System.out.println("[Porta]:");
        int porta = Console.readInt();
        conexao.conectarComCliente(endereco, porta);
        menuCliente();
    }

    /**
     * Método que solicita documento a um cliente específico!
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void solicitarUmDocumento() throws IOException, InterruptedException {
        System.out.println("Informe o id do documento que deseja solicitar para conferir");
        int idDoc = Console.readInt();

        JSONObject json = new JSONObject();
        json.put("command", "SolicitarDocumento");
        json.put("idDoc", idDoc);

        facade.novaMensagem(convertToByte(json.toString()));
        json = facade.getRespostaCartorio();

        facade.abrirConexaoCartorio();
        JSONObject requisicao = new JSONObject();
        requisicao.put("command", "ValidarDocumento");
        requisicao.put("cpfDono", json.getString("cpfDono"));
        requisicao.put("chavePublica", json.getString("chavePublica"));
        requisicao.put("arquivo", json.getString("arquivo"));
        requisicao.put("assinatura", json.getString("assinatura"));

        facade.novaMensagem(convertToByte(requisicao.toString()));
        requisicao = facade.getRespostaCartorio();
        System.out.println(requisicao.getString("status"));
    }

    /**
     * Método que solicita a transferência de um documento para um cliente,
     * alterando a posse do mesmo!
     *
     * @throws IOException
     */
    public void solicitarTransferencia() throws IOException {
        System.out.println("Informe o id do documento!");
        int idDoc = Console.readInt();
        System.out.println("Insira seu CPF para completar a transação");
        String cpf = Console.readString();

        JSONObject requisicao = new JSONObject();
        requisicao.put("command", "TransferirDocumento");
        requisicao.put("idDoc", idDoc);
        requisicao.put("cpfSolicitante", cpf);

        facade.novaMensagem(convertToByte(requisicao.toString()));
    }

    /**
     * Método que realiza um cadastro de documento selecionado na memória,
     * informando ao cartório a posse deste.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public void cadastrarDocumento() throws FileNotFoundException, IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String opc = null;
        do {
            try {
                byte[] arqvConvrt = garqs.selecionarArquivo();
                String byteString = new String(arqvConvrt, StandardCharsets.UTF_8);
                JSONObject json = new JSONObject();
                System.out.println("*****************************");
                System.out.println("Informe o seu CPF para identificação");
                System.out.println("[CPF]:");
                String cpf = Console.readString();

                json.put("command", "CadastrarDocumento");
                json.put("cpf", cpf);
                json.put("documento", byteString);
                facade.novaMensagem(convertToByte(json.toString()));
                facade.CadastrarDocumento(arqvConvrt);
                System.out.println("*****************************");
                System.out.println(facade.getRespostaCartorio().getString("status"));
                System.out.println("*****************************\n");
            } catch (IOException e) {
                System.out.println("Erro: " + e);
            }

            System.out.println("\nDeseja cadastrar outro documento? S/N");
            opc = Console.readString();
        } while (opc.compareTo("S") == 0 || opc.compareTo("s") == 0);
    }

    /**
     * Metódo para solicitação de informações sobre documentos ligados ao cpf
     * consultado!
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void buscarDocumento() throws IOException, InterruptedException {
        System.out.println("*****************************");
        System.out.println("Informe o Nº do CPF para obter o(s) documento(s)!");
        String cpf = Console.readString();

        JSONObject requisicao = new JSONObject();
        requisicao.put("command", "BuscarDocumento");
        requisicao.put("cpf", cpf);
        facade.novaMensagem(convertToByte(requisicao.toString()));

        JSONArray array = facade.getRespostaCartorio().getJSONArray("documentos");
        System.out.println(array.length());
        for (int i = 0; i < array.length(); i++) {
            Object o = array.get(i);
            if (!JSONObject.NULL.equals(o)) {
                JSONObject json;
                json = (JSONObject) o;
                System.out.println("\n*****************************");
                System.out.println("ID - Documento: " + json.getInt("id"));
                System.out.println("Dados do Documento:\n" + json.getString("documento"));
                System.out.println("*****************************\n");
                garqs.gravarArquivo("cpf" + cpf + "id" + i, convertToByte(json.getString("documento")));
            }

        }

    }

    /**
     * Método para cadastrar as informações pessoais no servidor/ cartório!
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void cadastroNoCartorio() throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
        String op;

        System.out.println("\nÉ necessário realizar um cadastro, para ter acesso!");

        System.out.println("Informe seu nome:");
        String nome = Console.readString();

        System.out.println("Informe seu sobrenome:");
        String sobrenome = Console.readString();

        System.out.println("Insira o número do seu cpf:");
        String cpf = Console.readString();

        System.out.println("Senha:");
        String senha = Console.readString();

        System.out.println("Informe o número do RG:");
        String rg = Console.readString();

        System.out.println("Informe um email e uma senha valida");
        System.out.println("Email:");
        String email = Console.readString();

        System.out.println("Informe seu Telefone:");
        String telefone = Console.readString();

        JSONObject dados = new JSONObject();

        dados.put("nome", nome);
        dados.put("sobrenome", sobrenome);
        dados.put("cpf", cpf);
        dados.put("rg", rg);
        dados.put("email", email);
        dados.put("telefone", telefone);
        dados.put("senha", senha);
        dados.put("command", "CadastroPerfil");

        facade.novaMensagem(convertToByte(dados.toString()));
        JSONObject json = facade.getRespostaCartorio();

        if (json.getString("status").compareTo("Perfil cadastrado com sucesso!") == 0) {
            facade.cadastrarPerfil(nome, sobrenome, cpf, convert.convertStringToPrivateKey(json.getString("chavePrivada")),
                    convert.convertStringToPublicKey(json.getString("chavePublica")));
            System.out.println("\n*****************************");
            System.out.println(json.getString("status"));
            System.out.println("*****************************\n");
            System.out.println("Agora, utilize seu CPF e senha para login!\n");
            loginNoCartorio();
        } else {
            System.out.println("*****************************");
            System.out.println(json.getString("status"));
            System.out.println("Tente Novamente!");
            Thread.sleep(1000);
            cadastroNoCartorio();
        }

    }

    /**
     * Método para realizar login no servidor/cartório!
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public void loginNoCartorio() throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("*****************************");
        System.out.println("Para realizar login:");
        System.out.println("Informe seu CPF");
        System.out.println("[CPF]");
        String cpf = Console.readString();
        System.out.println("Insira a senha cadastrada");
        String senha = Console.readString();
        System.out.println("*****************************");
        JSONObject dados = new JSONObject();
        dados.put("cpf", cpf);
        dados.put("senha", senha);
        dados.put("command", "Login");

        facade.novaMensagem(convertToByte(dados.toString()));
        dados = facade.getRespostaCartorio();
        if (dados.getString("status").compareTo("Login efetuado com sucesso!") == 0) {
            if (facade.getPerfil() == null) {
                Perfil p = new Perfil(dados.getString("nome"), dados.getString("sobrenome"), dados.getString("cpf"),
                        convert.convertStringToPrivateKey(dados.getString("chavePrivada")),
                        convert.convertStringToPublicKey(dados.getString("chavePublica")));
                facade.setPerfil(p);
            }
            System.out.println("\nLogin efetuado com sucesso!");
        } else {
            System.out.println("\n" + facade.getRespostaCartorio().getString("status"));
            loginNoCartorio();
        }
    }

    /**
     * Método responsável por gerenciar as ações iniciais do cliente em
     * execução!
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void acessoAoCartorio() throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
        conectarAoCartorio();
        System.out.println("\n*****************************\n");
        System.out.println("Possui perfil cadastrado no cartório? S/N");
        String resposta = Console.readString();
        if (resposta.equals("S") | resposta.equals("s")) {
            loginNoCartorio();
        } else {
            cadastroNoCartorio();
        }

    }

    /**
     * Método para criar um servidor interno para tratar as comunicações de
     * outros clientes.
     *
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public void inicializaServidor() throws IOException, FileNotFoundException, ClassNotFoundException {
        conexao.criarServidor();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, InvalidKeyException, SignatureException, ClassNotFoundException, IOException {
        Cliente pessoa = new Cliente();
        int repeat = 0;
        try {
            pessoa.inicializaServidor();
            pessoa.acessoAoCartorio(); //Conecta ao cartório para realizar login!
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

                        pessoa.buscarDocumento();

                        break;
                    case 3:

                        pessoa.conectarComUmCliente();
                        break;
                }
            } while (repeat == 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

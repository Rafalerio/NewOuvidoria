package menu;

import entity.Feedback;
import entity.Usuario;
import conexao.Conexao;
import dao.UsuarioDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;

/**
 * Classe principal do sistema de feedback.
 * Gerencia os menus interativos de usuário e administrador
 * Faz uso da conexão com banco e gerenciador de feedback.
 */
public class SistemaFeedback {

    private static Gerenciador gerenciador = new Gerenciador();
    private static UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instância de UsuarioDAO
    private static Scanner scanner = new Scanner(System.in);
    private static Connection conexao;
    private static Usuario usuarioLogado; // Armazena o usuário logado atualmente

    // Credenciais fixas do administrador
    private static final String ADMIN_LOGIN = "admin123";
    private static final String ADMIN_SENHA = "admin321";

    public static void main(String[] args) throws SQLException {
        // Estabelece conexão com o banco de dados
        conexao = Conexao.getConexao(); // Chamar o método getConexao para obter a conexão
        if (conexao != null) {
            System.out.println("Conectado ao banco de dados com sucesso.");
        } else {
            System.out.println("Falha ao conectar ao banco de dados.");
            return; // Encerrar se não houver conexão
        }

        boolean execucao = true;
        while (execucao) {
            System.out.println("\n=== Sistema de entity.Feedback ===");
            System.out.println("Selecione o papel:");
            System.out.println("1. Usuário");
            System.out.println("2. Administrador");
            System.out.println("3. Sair");
            System.out.print("Escolha sua opção: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    menuUsuario();
                    break;
                case "2":
                    menuAdmin();
                    break;
                case "3":
                    execucao = false;
                    System.out.println("Encerrando o sistema.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        try {
            // Fechar a conexão ao sair do sistema
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Menu principal para o usuário com opções de cadastro, login e enviar feedback.

    private static void menuUsuario() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu Usuário ---");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Login");
            System.out.println("3. Voltar");
            System.out.print("Escolha sua opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarUsuario();
                    break;
                case "2":
                    if (loginUsuario()) {
                        menuEnviarFeedback();
                    }
                    break;
                case "3":
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    // Menu para envio de feedback após usuário estar logado.

    private static void menuEnviarFeedback() {
        if (usuarioLogado == null) {
            System.out.println("Nenhum usuário logado. Por favor, faça login primeiro.");
            return;
        }

        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu Feedback ---");
            System.out.println("Usuário logado: " + usuarioLogado.getUsuario());
            System.out.println("1. Enviar novo feedback");
            System.out.println("2. Ver meus feedbacks");
            System.out.println("3. Atualizar meu feedback");
            System.out.println("4. Excluir meu feedback");
            System.out.println("5. Sair");
            System.out.print("Escolha sua opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    enviarNovoFeedback();
                    break;
                case "2":
                    verMeusFeedbacks();
                    break;
                case "3":
                    atualizarMeuFeedback();
                    break;
                case "4":
                    deletarMeuFeedback();
                    break;
                case "5":
                    sair = true;
                    usuarioLogado = null; // Desloga o usuário ao sair
                    System.out.println("Você foi desconectado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void enviarNovoFeedback() {
        // Usar nome e departamento do usuário logado, não pedir novamente
        String nome = usuarioLogado.getNome();
        String departamento = usuarioLogado.getDepartamento();

        System.out.print("Digite seu feedback: ");
        String textoFeedback = scanner.nextLine().trim();

        if (textoFeedback.isEmpty()) {
            System.out.println("O texto do feedback é obrigatório. Tente novamente.");
        } else {
            gerenciador.registrarFeedback(nome, departamento, textoFeedback);
        }
    }

   // Exibe feedbacks do usuário logado
    private static void verMeusFeedbacks() {
        System.out.println("\n--- Meus Feedbacks ---");
        List<entity.Feedback> todosFeedbacks = gerenciador.listarFeedback();
        boolean encontrou = false;
        for (entity.Feedback f : todosFeedbacks) {
            // Compara o nome e o departamento do feedback com os dados do usuário logado
            if (f.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                    f.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                System.out.println(f.toString());
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Você não possui feedbacks registrados.");
        }
    }

    // Permite ao usuário atualizar seu feedback
    private static void atualizarMeuFeedback() {
        System.out.print("Digite o ID do feedback que deseja atualizar: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);

            if (feedback != null) {
                // Checa se o feedback pertence ao usuário logado
                if (feedback.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                        feedback.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                    System.out.println("Feedback atual: " + feedback.getTextoFeedback());
                    System.out.print("Digite o novo texto para o feedback: ");
                    String novoTexto = scanner.nextLine().trim();
                    if (novoTexto.isEmpty()) {
                        System.out.println("O texto do feedback não pode ser vazio.");
                    } else {
                        gerenciador.atualizarFeedback(id, novoTexto);
                    }
                } else {
                    System.out.println("Você não tem permissão para atualizar este feedback. Você só pode alterar seus próprios feedbacks.");
                }
            } else {
                System.out.println("Feedback com ID " + id + " não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
        }
    }

    // Permite ao usuário deletar seu feedback
    private static void deletarMeuFeedback() {
        System.out.print("Digite o ID do feedback que deseja excluir: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);

            if (feedback != null) {
                // Checa se o feedback pertence ao usuário logado
                if (feedback.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                        feedback.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                    System.out.print("Tem certeza que deseja excluir o feedback? (s/n): ");
                    String confirmacao = scanner.nextLine().toLowerCase();
                    if (confirmacao.equals("s")) {
                        gerenciador.deletarFeedback(id);
                    } else {
                        System.out.println("Exclusão cancelada.");
                    }
                } else {
                    System.out.println("Você não tem permissão para excluir este feedback. Você só pode excluir seus próprios feedbacks.");
                }
            } else {
                System.out.println("Feedback com ID " + id + " não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
        }
    }

    // Processo de cadastro no banco via UsuarioDAO
    private static void cadastrarUsuario() {
        System.out.print("Digite o nome completo: ");
        String nomeCompleto = scanner.nextLine().trim();
        if (nomeCompleto.isEmpty()) {
            System.out.println("Nome completo não pode ser vazio.");
            return;
        }

        System.out.print("Digite o nome de usuário (login): ");
        String login = scanner.nextLine().trim();
        if (login.isEmpty()) {
            System.out.println("Nome de usuário (login) não pode ser vazio.");
            return;
        }

        if (usuarioDAO.verificarUsuarioExistente(login)) {
            System.out.println("Usuário já cadastrado. Tente outro nome de usuário.");
            return;
        }

        System.out.print("Digite o email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email não pode ser vazio.");
            return;
        }

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine().trim();
        if (senha.isEmpty()) {
            System.out.println("Senha não pode ser vazia.");
            return;
        }

        System.out.print("Digite a função (ex: funcionário, gestor): ");
        String funcao = scanner.nextLine().trim();
        if (funcao.isEmpty()) {
            System.out.println("Função não pode ser vazia.");
            return;
        }

        System.out.print("Digite o departamento: ");
        String departamento = scanner.nextLine().trim();
        if (departamento.isEmpty()) {
            System.out.println("Departamento não pode ser vazio.");
            return;
        }

        Usuario novoUsuario = new Usuario(nomeCompleto, login, email, senha, funcao, departamento);

        usuarioDAO.cadastrarUsuario(novoUsuario);
        System.out.println("Cadastro realizado com sucesso.");
    }


    /**
     * Processo de login pelo banco via UsuarioDAO.
     * @return boolean true se login usuário válido
     */
    private static boolean loginUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String login = scanner.nextLine().trim();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine().trim();

        Usuario credenciais = new Usuario(login, senha);

        Usuario usuarioAutenticado = usuarioDAO.loginUsuario(credenciais);

        if (usuarioAutenticado != null) {
            usuarioLogado = usuarioAutenticado;
            System.out.println("Login de usuário realizado com sucesso. Bem-vindo, " + usuarioLogado.getNome() + "!");
            return true;
        } else {
            System.out.println("Usuário ou senha incorretos.");
            return false;
        }
    }

    // Menu e login do administrador com credenciais fixas.

    private static void menuAdmin() {
        System.out.print("Digite o nome de usuário do administrador: ");
        String usuarioAdmin = scanner.nextLine().trim();
        System.out.print("Digite a senha do administrador: ");
        String senhaAdmin = scanner.nextLine().trim();

        // Verificação das credenciais fixas
        if (usuarioAdmin.equals(ADMIN_LOGIN) && senhaAdmin.equals(ADMIN_SENHA)) {
            System.out.println("Login de administrador realizado com sucesso.");
            menuAdminOperacoes();
        } else {
            System.out.println("Falha no login de administrador. Verifique as credenciais.");
        }
    }

    // Menu de operações administrativas após login bem-sucedido.

    private static void menuAdminOperacoes() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu Administrador ---");
            System.out.println("1. Listar todos os feedbacks");
            System.out.println("2. Buscar feedback por ID");
            System.out.println("3. Buscar por nome/departamento/texto");
            System.out.println("4. Excluir feedback"); // Removida a opção de atualizar
            System.out.println("5. Exportar feedback para arquivo");
            System.out.println("6. Sair");
            System.out.print("Escolha sua opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    listarFeedbacksAdmin();
                    break;
                case "2":
                    buscarFeedbackPorIdAdmin();
                    break;
                case "3":
                    buscarFeedbackAdminPorCriterio();
                    break;
                case "4":
                    deletarFeedbackAdmin();
                    break;
                case "5":
                    exportarFeedbacksAdmin();
                    break;
                case "6":
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


    private static void listarFeedbacksAdmin() {
        System.out.println("\n--- Lista de Feedbacks (Mais Recente Primeiro) ---");
        List<Feedback> feedbacks = gerenciador.listarFeedback();
        if (feedbacks.isEmpty()) {
            System.out.println("Nenhum feedback registrado.");
            return;
        }
        for (Feedback f : feedbacks) {
            System.out.println(f.toString());
        }
    }

    private static void buscarFeedbackPorIdAdmin() {
        System.out.print("Digite o ID do feedback para busca: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            Feedback f = gerenciador.buscarFeedbackPorId(id);
            if (f != null) {
                System.out.println(f.toString());
            } else {
                System.out.println("Feedback não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
        }
    }

    private static void deletarFeedbackAdmin() {
        System.out.print("Digite o ID do feedback para deletar: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            Feedback f = gerenciador.buscarFeedbackPorId(id);
            if (f != null) {
                gerenciador.deletarFeedback(id);
            } else {
                System.out.println("Feedback não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
        }
    }

    private static void buscarFeedbackAdminPorCriterio() {
        System.out.print("Digite um termo para a busca (nome, departamento ou texto): ");
        String termoBusca = scanner.nextLine().trim();
        if (termoBusca.isEmpty()) {
            System.out.println("O termo de busca não pode ser vazio.");
            return;
        }
        System.out.println("\n--- Resultados da Busca ---");
        List<entity.Feedback> resultados = gerenciador.buscarFeedbacksPorCriterio(termoBusca);
        if (resultados.isEmpty()) {
            System.out.println("Nenhum feedback encontrado para o termo '" + termoBusca + "'.");
        } else {
            for (entity.Feedback f : resultados) {
                System.out.println(f.toString());
            }
        }
    }

    private static void exportarFeedbacksAdmin() {
        System.out.print("Digite o nome do arquivo para exportação (exemplo: feedbacks.txt): ");
        String nomeArquivo = scanner.nextLine().trim();
        if (nomeArquivo.isEmpty()) {
            System.out.println("Nome de arquivo inválido. Não pode ser vazio.");
            return;
        }
        try {
            gerenciador.exportarFeedbackParaArquivo(nomeArquivo);
            System.out.println("Exportação realizada com sucesso para " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro na exportação: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace para depuração
        }
    }
}
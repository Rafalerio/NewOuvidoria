import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Classe principal do sistema de feedback.
 * Gerencia os menus interativos de usuário e administrador
 * Faz uso da conexão com banco e gerenciador de feedback.
 */
public class SistemaFeedback {

    private static GerenciadorFeedback gerenciadorFeedback = new GerenciadorFeedback();
    private static UsuarioDAO usuarioDAO;
    private static Scanner scanner = new Scanner(System.in);
    private static Connection conexao;

    public static void main(String[] args) {
        try {
            // Estabelece conexão com o banco de dados
            conexao = ConexaoBancoDados.getConnection();
            usuarioDAO = new UsuarioDAO(conexao);
            System.out.println("Conectado ao banco de dados com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        boolean execucao = true;
        while (execucao) {
            System.out.println("\n=== Sistema de Feedback ===");
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
                    System.out.println("Encerrando o sistema. Obrigado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    /**
     * Menu principal para o usuário com opções de cadastro, login e enviar feedback.
     */
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
                        // Usuário logado, permite enviar feedback
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

    /**
     * Menu para envio de feedback após usuário estar logado.
     */
    private static void menuEnviarFeedback() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu Feedback ---");
            System.out.println("1. Enviar feedback");
            System.out.println("2. Sair");
            System.out.print("Escolha sua opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Digite seu nome: ");
                    String nome = scanner.nextLine().trim();
                    System.out.print("Digite seu departamento: ");
                    String departamento = scanner.nextLine().trim();
                    System.out.print("Digite seu feedback: ");
                    String textoFeedback = scanner.nextLine().trim();
                    if (nome.isEmpty() || departamento.isEmpty() || textoFeedback.isEmpty()) {
                        System.out.println("Todos os campos são obrigatórios. Tente novamente.");
                    } else {
                        gerenciadorFeedback.registrarFeedback(nome, departamento, textoFeedback);
                        System.out.println("Feedback enviado com sucesso.");
                    }
                    break;
                case "2":
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Processo de cadastro no banco via UsuarioDAO.
     */
    private static void cadastrarUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String usuario = scanner.nextLine().trim();
        if (usuario.isEmpty()) {
            System.out.println("Usuário não pode ser vazio.");
            return;
        }
        if (usuarioDAO.verificarUsuarioExistente(usuario)) {
            System.out.println("Usuário já cadastrado. Tente outro nome.");
            return;
        }

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine().trim();
        if (senha.isEmpty()) {
            System.out.println("Senha não pode ser vazia.");
            return;
        }

        if (usuarioDAO.cadastrarUsuario(usuario, senha)) {
            System.out.println("Cadastro realizado com sucesso.");
        } else {
            System.out.println("Erro no cadastro. Tente novamente.");
        }
    }

    /**
     * Processo de login pelo banco via UsuarioDAO.
     * @return boolean true se login usuário válido
     */
    private static boolean loginUsuario() {
        System.out.print("Digite o nome de usuário: ");
        String usuario = scanner.nextLine().trim();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine().trim();

        if (usuarioDAO.loginUsuario(usuario, senha)) {
            System.out.println("Login de usuário realizado com sucesso.");
            return true;
        } else {
            System.out.println("Usuário ou senha incorretos.");
            return false;
        }
    }

    /**
     * Menu e login do administrador.
     */
    private static void menuAdmin() {
        System.out.print("Digite o nome de usuário do administrador: ");
        String usuario = scanner.nextLine().trim();
        System.out.print("Digite a senha do administrador: ");
        String senha = scanner.nextLine().trim();

        if (usuarioDAO.loginAdmin(usuario, senha)) {
            menuAdminOperacoes();
        } else {
            System.out.println("Falha no login de administrador.");
        }
    }

    /**
     * Menu de operações administrativas após login bem-sucedido.
     */
    private static void menuAdminOperacoes() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n--- Menu Administrador ---");
            System.out.println("1. Listar feedbacks");
            System.out.println("2. Buscar feedback por ID");
            System.out.println("3. Atualizar feedback");
            System.out.println("4. Excluir feedback");
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
                    atualizarFeedbackAdmin();
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
        System.out.println("\n--- Lista de Feedbacks (Ordem Cronológica Reversa) ---");
        for (Feedback f : gerenciadorFeedback.listarFeedback()) {
            System.out.println(f.toString());
        }
    }

    private static void buscarFeedbackPorIdAdmin() {
        System.out.print("Digite o ID do feedback para busca: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            Feedback f = gerenciadorFeedback.buscarFeedbackPorId(id);
            if (f != null) {
                System.out.println(f.toString());
            } else {
                System.out.println("Feedback não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private static void atualizarFeedbackAdmin() {
        System.out.print("Digite o ID do feedback para atualizar: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            Feedback f = gerenciadorFeedback.buscarFeedbackPorId(id);
            if (f != null) {
                System.out.println("Feedback atual: " + f.getTextoFeedback());
                System.out.print("Digite o novo texto do feedback: ");
                String novoTexto = scanner.nextLine();
                if (novoTexto.trim().isEmpty()) {
                    System.out.println("Texto inválido.");
                } else {
                    gerenciadorFeedback.atualizarFeedback(id, novoTexto);
                    System.out.println("Feedback atualizado com sucesso.");
                }
            } else {
                System.out.println("Feedback não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private static void deletarFeedbackAdmin() {
        System.out.print("Digite o ID do feedback para deletar: ");
        String entrada = scanner.nextLine();
        try {
            int id = Integer.parseInt(entrada);
            Feedback f = gerenciadorFeedback.buscarFeedbackPorId(id);
            if (f != null) {
                gerenciadorFeedback.deletarFeedback(id);
                System.out.println("Feedback deletado com sucesso.");
            } else {
                System.out.println("Feedback não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private static void exportarFeedbacksAdmin() {
        System.out.print("Digite o nome do arquivo para exportação (exemplo: feedbacks.txt): ");
        String nomeArquivo = scanner.nextLine().trim();
        if (nomeArquivo.isEmpty()) {
            System.out.println("Nome de arquivo inválido.");
            return;
        }
        try {
            gerenciadorFeedback.exportarFeedbackParaArquivo(nomeArquivo);
            System.out.println("Exportação realizada com sucesso para " + nomeArquivo);
        } catch (Exception e) {
            System.out.println("Erro na exportação: " + e.getMessage());
        }
    }
}


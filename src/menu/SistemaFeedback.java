package menu;

import entity.Usuario;
import conexao.Conexao;
import dao.UsuarioDAO;
import java.sql.Connection;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Classe principal do sistema de feedback com interface gráfica.
 * Gerencia os diferentes painéis (menu inicial, login, usuário, administrador).
 */
public class SistemaFeedback extends JFrame {

    private static Gerenciador gerenciador = new Gerenciador(); // Gerenciador para operações de feedback
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();   // DAO para operações de usuário
    private static Connection conexao;                         // Conexão com o banco de dados
    private static Usuario usuarioLogado;                      // Usuário atualmente logado

    private JPanel cards; // Um painel que usa CardLayout

    // Constantes para os nomes dos painéis no CardLayout
    public static final String INITIAL_MENU_PANEL = "Menu Inicial";
    public static final String COLABORADORES_PANEL = "Colaboradores";
    public static final String ADMIN_LOGIN_PANEL = "AdminLogin";
    public static final String USER_PANEL = "Usuario";
    public static final String ADMIN_PANEL = "Administrador";

    private MenuInicialPanel menuInicialPanel;
    private ColaboradoresPanel colaboradoresPanel; 
    private AdminLoginPanel adminLoginPanel;     
    private UserPanel userPanel;
    private AdminPanel adminPanel;

    public SistemaFeedback() {
        setTitle("Sistema de Feedback");
        setSize(1000, 700); // Tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Estabelece conexão com o banco de dados
        conexao = Conexao.getConexao();
        if (conexao == null) {
            JOptionPane.showMessageDialog(this,
                    "Falha ao conectar ao banco de dados.",
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Encerra a aplicação se não houver conexão
            return;
        }

        cards = new JPanel(new CardLayout());

        // Inicializa os painéis
        menuInicialPanel = new MenuInicialPanel(this);
        colaboradoresPanel = new ColaboradoresPanel(this); // Passa a referência da JFrame
        adminLoginPanel = new AdminLoginPanel(this);       
        userPanel = new UserPanel(this);                   // Passa a referência da JFrame
        adminPanel = new AdminPanel(this);                

        // Adiciona os painéis ao CardLayout
        cards.add(menuInicialPanel, INITIAL_MENU_PANEL);
        cards.add(colaboradoresPanel, COLABORADORES_PANEL);
        cards.add(adminLoginPanel, ADMIN_LOGIN_PANEL);
        cards.add(userPanel, USER_PANEL);
        cards.add(adminPanel, ADMIN_PANEL);

        add(cards); // Adiciona o painel com CardLayout ao JFrame

        showPanel(INITIAL_MENU_PANEL); // Começa mostrando o menu inicial
    }

    /**
     * Alterna entre os painéis visíveis.
     * @param panelName O nome do painel a ser mostrado (constantes definidas acima).
     */
    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, panelName);
        if (panelName.equals(USER_PANEL)) {
            userPanel.loadUserFeedbacks(); // Carrega feedbacks do usuário ao entrar no painel
        } else if (panelName.equals(ADMIN_PANEL)) {
            adminPanel.loadAllFeedbacks(); // Carrega todos os feedbacks ao entrar no painel admin
        }
    }

    /**
     * Tenta realizar o login de um usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
    public boolean loginUsuario(String login, String senha) {
        Usuario credenciais = new Usuario(login, senha); // Construtor de Usuario deve aceitar login e senha
        usuarioLogado = usuarioDAO.loginUsuario(credenciais);
        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(this,
                    "Login de usuário realizado com sucesso. Bem-vindo, " + usuarioLogado.getNome() + "!",
                    "Login Bem-Sucedido", JOptionPane.INFORMATION_MESSAGE);
            userPanel.setWelcomeMessage("Bem-vindo, " + usuarioLogado.getNome());
            showPanel(USER_PANEL);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha incorretos.",
                    "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Tenta realizar o login de um administrador.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
    public boolean loginAdmin(String login, String senha) {
        // Credenciais fixas do administrador
        String ADMIN_LOGIN = "admin123";
        String ADMIN_SENHA = "admin321";

        if (login.equals(ADMIN_LOGIN) && senha.equals(ADMIN_SENHA)) {
            JOptionPane.showMessageDialog(this,
                    "Login de administrador realizado com sucesso.",
                    "Login Bem-Sucedido", JOptionPane.INFORMATION_MESSAGE);
            showPanel(ADMIN_PANEL);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha de administrador incorretos.",
                    "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Tenta cadastrar um novo usuário.
     * @return true se o cadastro for bem-sucedido, false caso contrário.
     */
    public boolean cadastrarUsuario(String nomeCompleto, String login, String email, String senha, String funcao, String departamento) {
        if (nomeCompleto.isEmpty() || login.isEmpty() || email.isEmpty() || senha.isEmpty() || funcao.isEmpty() || departamento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios para o cadastro.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (usuarioDAO.verificarUsuarioExistente(login)) {
            JOptionPane.showMessageDialog(this, "Usuário já cadastrado. Tente outro nome de usuário.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Usuario novoUsuario = new Usuario(nomeCompleto, login, email, senha, funcao, departamento);
        usuarioDAO.cadastrarUsuario(novoUsuario);
        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    // Métodos para operações de feedback (chamados pelos painéis de usuário/admin)

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Desloga o usuário atual e retorna para a tela de menu inicial.

    public void logout() {
        usuarioLogado = null; // Define o usuário logado como nulo
        showPanel(INITIAL_MENU_PANEL); // Volta para o painel do menu inicial
        JOptionPane.showMessageDialog(this, "Você foi desconectado.", "Logout", JOptionPane.INFORMATION_MESSAGE);
    }

    // Métodos delegados ao gerenciador (para serem chamados pelos painéis)
    public void registrarFeedback(String textoFeedback) {
        if (usuarioLogado != null) {
            gerenciador.registrarFeedback(usuarioLogado.getNome(), usuarioLogado.getDepartamento(), textoFeedback);
            JOptionPane.showMessageDialog(this, "Feedback enviado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum usuário logado para enviar feedback.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<entity.Feedback> listarMeusFeedbacks() {
        if (usuarioLogado == null) {
            return new java.util.ArrayList<>(); // Retorna lista vazia se não há usuário logado
        }
        List<entity.Feedback> todosFeedbacks = gerenciador.listarFeedback(); // busca do Banco de dados
        List<entity.Feedback> meusFeedbacks = new java.util.ArrayList<>();
        for (entity.Feedback f : todosFeedbacks) {
            // Verifica pelo nome e departamento
            if (f.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                    f.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                meusFeedbacks.add(f);
            }
        }
        return meusFeedbacks;
    }

    public entity.Feedback buscarMeuFeedbackPorId(int id) {
        if (usuarioLogado == null) {
            return null;
        }
        entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);
        if (feedback != null &&
                feedback.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                feedback.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
            return feedback;
        }
        return null;
    }

    public void atualizarMeuFeedback(int id, String novoTexto) {
        entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);
        if (feedback != null) {
            if (feedback.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                    feedback.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                gerenciador.atualizarFeedback(id, novoTexto);
                JOptionPane.showMessageDialog(this, "Feedback atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para atualizar este feedback.", "Permissão Negada", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Feedback com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletarMeuFeedback(int id) {
        entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);
        if (feedback != null) {
            if (feedback.getNome().equalsIgnoreCase(usuarioLogado.getNome()) &&
                    feedback.getDepartamento().equalsIgnoreCase(usuarioLogado.getDepartamento())) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o feedback " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gerenciador.deletarFeedback(id);
                    JOptionPane.showMessageDialog(this, "Feedback deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para excluir este feedback.", "Permissão Negada", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Feedback com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos para Admin
    public List<entity.Feedback> listarTodosFeedbacks() {
        return gerenciador.listarFeedback(); // Agora busca do DB
    }

    public entity.Feedback buscarFeedbackAdminPorId(int id) {
        return gerenciador.buscarFeedbackPorId(id); // Busca diretamente do banco de dados
    }

    public void deletarFeedbackAdmin(int id) {
        entity.Feedback feedback = gerenciador.buscarFeedbackPorId(id);
        if (feedback != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o feedback " + id + " (ADMIN)?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                gerenciador.deletarFeedback(id);
                JOptionPane.showMessageDialog(this, "Feedback (ADMIN) deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Feedback com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<entity.Feedback> buscarFeedbacksAdminPorCriterio(String termoBusca) {
        return gerenciador.buscarFeedbacksPorCriterio(termoBusca); // Busca do DB
    }

    public void exportarFeedbacksAdmin(String nomeArquivo) {
        try {
            gerenciador.exportarFeedbackParaArquivo(nomeArquivo);
            JOptionPane.showMessageDialog(this, "Exportação realizada com sucesso para " + nomeArquivo, "Exportação", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na exportação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Garante que a GUI seja executada
        SwingUtilities.invokeLater(() -> {
            new SistemaFeedback().setVisible(true);
        });
    }
}
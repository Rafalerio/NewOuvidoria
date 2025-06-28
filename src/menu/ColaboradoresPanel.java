package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel para a interface de Login e Cadastro de Usuário (Colaboradores).
 */
public class ColaboradoresPanel extends JPanel { // Renomeado de LoginPanel

    private SistemaFeedback mainFrame; // Referência para o JFrame principal
    private JTextField loginField;
    private JPasswordField passwordField;

    // Campos para cadastro
    private JTextField regNomeField;
    private JTextField regLoginField;
    private JTextField regEmailField;
    private JPasswordField regSenhaField;
    private JTextField regFuncaoField;
    private JTextField regDepartamentoField;

    public ColaboradoresPanel(SistemaFeedback mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout()); // Usa BorderLayout para organizar os sub-painéis

        // --- Painel de Login ---
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Grid para campos e botões
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login de Usuário Existente"));
        loginPanel.add(new JLabel("Login:"));
        loginField = new JTextField(20);
        loginPanel.add(loginField);
        loginPanel.add(new JLabel("Senha:"));
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField);

        JPanel loginButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginUserButton = new JButton("Entrar");

        loginUserButton.addActionListener(e -> {
            String login = loginField.getText();
            String senha = new String(passwordField.getPassword());
            if (mainFrame.loginUsuario(login, senha)) {
                // Limpa campos após login bem-sucedido
                loginField.setText("");
                passwordField.setText("");
            }
        });
        loginButtons.add(loginUserButton);
        loginPanel.add(loginButtons); // Adiciona os botões no grid como se fossem um único componente


        // --- Painel de Cadastro ---
        JPanel registerPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Grid para campos
        registerPanel.setBorder(BorderFactory.createTitledBorder("Novo Cadastro de Usuário"));
        registerPanel.add(new JLabel("Nome Completo:"));
        regNomeField = new JTextField(20);
        registerPanel.add(regNomeField);
        registerPanel.add(new JLabel("Login (Nome de Usuário):"));
        regLoginField = new JTextField(20);
        registerPanel.add(regLoginField);
        registerPanel.add(new JLabel("Email:"));
        regEmailField = new JTextField(20);
        registerPanel.add(regEmailField);
        registerPanel.add(new JLabel("Senha:"));
        regSenhaField = new JPasswordField(20);
        registerPanel.add(regSenhaField);
        registerPanel.add(new JLabel("Função:"));
        regFuncaoField = new JTextField(20);
        registerPanel.add(regFuncaoField);
        registerPanel.add(new JLabel("Departamento:"));
        regDepartamentoField = new JTextField(20);
        registerPanel.add(regDepartamentoField);

        JButton registerButton = new JButton("Cadastrar Usuário");
        registerButton.addActionListener(e -> {
            String nome = regNomeField.getText();
            String login = regLoginField.getText();
            String email = regEmailField.getText();
            String senha = new String(regSenhaField.getPassword());
            String funcao = regFuncaoField.getText();
            String departamento = regDepartamentoField.getText();
            if (mainFrame.cadastrarUsuario(nome, login, email, senha, funcao, departamento)) {
                // Limpa os campos após o cadastro (opcional)
                regNomeField.setText("");
                regLoginField.setText("");
                regEmailField.setText("");
                regSenhaField.setText("");
                regFuncaoField.setText("");
                regDepartamentoField.setText("");
            }
        });
        JPanel registerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerButtonPanel.add(registerButton);
        registerPanel.add(registerButtonPanel); // Adiciona o botão no grid

        // Painel para conter Login e Cadastro lado a lado ou um em cima do outro
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Organiza verticalmente
        formPanel.add(loginPanel);
        formPanel.add(Box.createVerticalStrut(20)); // Espaçamento
        formPanel.add(registerPanel);

        add(formPanel, BorderLayout.CENTER);

        // Botão Voltar
        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> {
            // Limpa os campos ao voltar
            loginField.setText("");
            passwordField.setText("");
            regNomeField.setText("");
            regLoginField.setText("");
            regEmailField.setText("");
            regSenhaField.setText("");
            regFuncaoField.setText("");
            regDepartamentoField.setText("");
            mainFrame.showPanel(SistemaFeedback.INITIAL_MENU_PANEL);
        });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(backButton);
        add(southPanel, BorderLayout.SOUTH);

        // Ajustes para centralizar e dar padding
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }
}
package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


 // Painel para a interface de Login do Administrador.

public class AdminLoginPanel extends JPanel {

    private SistemaFeedback mainFrame; // Referência para o JFrame principal
    private JTextField loginField;
    private JPasswordField passwordField;

    public AdminLoginPanel(SistemaFeedback mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout()); // Usar GridBagLayout para centralizar

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login do Administrador");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reseta para uma coluna
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        loginField = new JTextField(20);
        add(loginField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Entrar como Administrador");
        add(loginButton, gbc);

        gbc.gridy = 4;
        JButton backButton = new JButton("Voltar");
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda
        add(backButton, gbc);

        // Ações dos botões
        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String senha = new String(passwordField.getPassword());
            if (mainFrame.loginAdmin(login, senha)) {
                // Limpa campos após login bem-sucedido
                loginField.setText("");
                passwordField.setText("");
            }
        });

        backButton.addActionListener(e -> {
            // Limpa os campos ao voltar
            loginField.setText("");
            passwordField.setText("");
            mainFrame.showPanel(SistemaFeedback.INITIAL_MENU_PANEL);
        });
    }
}
package menu;

import javax.swing.*;
import java.awt.*;


 // Painel inicial com opções de "Colaboradores" e "Administração".

public class MenuInicialPanel extends JPanel {

    private SistemaFeedback mainFrame; // Referência para o JFrame principal

    public MenuInicialPanel(SistemaFeedback mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout()); // Usar GridBagLayout para centralizar

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Padding entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20; // Aumenta a altura dos botões

        JLabel titleLabel = new JLabel("Bem-vindo ao Sistema de Feedback!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa duas colunas
        add(titleLabel, gbc);

        JButton colaboradoresButton = new JButton("Colaboradores");
        colaboradoresButton.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(colaboradoresButton, gbc);

        JButton adminButton = new JButton("Administração");
        adminButton.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(adminButton, gbc);

        // Ações dos botões
        colaboradoresButton.addActionListener(e -> mainFrame.showPanel(SistemaFeedback.COLABORADORES_PANEL));
        adminButton.addActionListener(e -> mainFrame.showPanel(SistemaFeedback.ADMIN_LOGIN_PANEL));
    }
}
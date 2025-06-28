package menu;

import entity.Feedback;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Painel para a interface de usuário logado.
 */
public class UserPanel extends JPanel {

    private SistemaFeedback mainFrame; // Referência para o JFrame principal
    private JLabel welcomeLabel;
    private JTextArea feedbackTextArea;
    private JTextField feedbackIdField; // Para atualizar/excluir
    private JTable feedbackTable;
    private DefaultTableModel tableModel;

    // Novo campo e botão para buscar por ID
    private JTextField searchIdField;
    private JButton searchByIdButton;
    private JTextArea singleFeedbackDisplayArea; // Para mostrar um único feedback buscado por ID

    public UserPanel(SistemaFeedback mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10)); // Layout principal do painel de usuário
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel = new JLabel("Bem-vindo, Usuário!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // --- Painel de Envio de Feedback ---
        JPanel sendPanel = new JPanel(new BorderLayout(5, 5));
        sendPanel.setBorder(BorderFactory.createTitledBorder("Enviar Novo Feedback"));
        feedbackTextArea = new JTextArea(5, 30);
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackTextArea);
        sendPanel.add(scrollPane, BorderLayout.CENTER);

        JButton sendButton = new JButton("Enviar Feedback");
        sendButton.addActionListener(e -> {
            String texto = feedbackTextArea.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O feedback não pode ser vazio.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mainFrame.registrarFeedback(texto);
            feedbackTextArea.setText(""); // Limpa o campo após o envio
            loadUserFeedbacks(); // Recarrega a lista de feedbacks
        });
        sendPanel.add(sendButton, BorderLayout.SOUTH);

        // --- Painel de Visualização e Ações de Feedback ---
        JPanel viewActionsPanel = new JPanel(new BorderLayout(5, 5));
        viewActionsPanel.setBorder(BorderFactory.createTitledBorder("Meus Feedbacks"));

        // Tabela para exibir feedbacks
        String[] columnNames = {"ID", "Nome", "Departamento", "Feedback", "Data/Hora"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células da tabela não editáveis
            }
        };
        feedbackTable = new JTable(tableModel);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Apenas uma linha pode ser selecionada
        JScrollPane tableScrollPane = new JScrollPane(feedbackTable);
        viewActionsPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Painel de botões de ação (Atualizar/Excluir) e busca por ID
        JPanel actionButtonsWrapperPanel = new JPanel(); // Um wrapper para melhor organização
        actionButtonsWrapperPanel.setLayout(new BoxLayout(actionButtonsWrapperPanel, BoxLayout.Y_AXIS)); // Organiza verticalmente

        // Primeira linha de botões (IDs e Ações)
        JPanel topActionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topActionRow.add(new JLabel("ID Sel./Ação:"));
        feedbackIdField = new JTextField(5);
        topActionRow.add(feedbackIdField);
        JButton updateButton = new JButton("Atualizar Selecionado");
        JButton deleteButton = new JButton("Excluir Selecionado");
        topActionRow.add(updateButton);
        topActionRow.add(deleteButton);
        actionButtonsWrapperPanel.add(topActionRow);

        // Segunda linha de botões (Busca e Recarregar)
        JPanel midActionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton refreshButton = new JButton("Recarregar Meus Feedbacks");
        midActionRow.add(refreshButton);
        midActionRow.add(new JSeparator(SwingConstants.VERTICAL));
        midActionRow.add(new JLabel("Buscar por ID:"));
        searchIdField = new JTextField(5);
        midActionRow.add(searchIdField);
        JButton searchByIdButton = new JButton("Buscar Feedback por ID");
        midActionRow.add(searchByIdButton);
        actionButtonsWrapperPanel.add(midActionRow);

        // Terceira linha de botões (Logout)
        JPanel bottomActionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Alinha o logout à direita
        JButton logoutButton = new JButton("Logout");
        bottomActionRow.add(logoutButton);
        actionButtonsWrapperPanel.add(bottomActionRow);

        viewActionsPanel.add(actionButtonsWrapperPanel, BorderLayout.SOUTH); // Adiciona o wrapper ao painel

        // Área para exibir feedback individualmente após busca por ID
        singleFeedbackDisplayArea = new JTextArea(3, 40);
        singleFeedbackDisplayArea.setEditable(false);
        singleFeedbackDisplayArea.setLineWrap(true);
        singleFeedbackDisplayArea.setWrapStyleWord(true);
        JScrollPane singleFeedbackScrollPane = new JScrollPane(singleFeedbackDisplayArea);
        viewActionsPanel.add(singleFeedbackScrollPane, BorderLayout.NORTH); // Adiciona ao norte do painel de visualização


        // Adicionar Listeners aos botões de ação
        feedbackTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && feedbackTable.getSelectedRow() != -1) {
                int selectedRow = feedbackTable.getSelectedRow();
                Object id = tableModel.getValueAt(selectedRow, 0);
                feedbackIdField.setText(id.toString());
                searchIdField.setText(id.toString());
            }
        });

        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(feedbackIdField.getText());
                String novoTexto = JOptionPane.showInputDialog(this, "Digite o novo texto para o feedback ID " + id + ":", "Atualizar Feedback", JOptionPane.PLAIN_MESSAGE);
                if (novoTexto != null && !novoTexto.trim().isEmpty()) {
                    mainFrame.atualizarMeuFeedback(id, novoTexto.trim());
                    loadUserFeedbacks();
                } else if (novoTexto != null) { // Usuário digitou vazio e deu OK
                    JOptionPane.showMessageDialog(this, "O texto do feedback não pode ser vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um ID válido ou selecione na tabela para atualizar.", "Erro de ID", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(feedbackIdField.getText());
                mainFrame.deletarMeuFeedback(id);
                loadUserFeedbacks();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um ID válido ou selecione na tabela para excluir.", "Erro de ID", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> loadUserFeedbacks());
        logoutButton.addActionListener(e -> mainFrame.logout());

        // Listener para o botão de busca por ID
        searchByIdButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(searchIdField.getText());
                Feedback foundFeedback = mainFrame.buscarMeuFeedbackPorId(id);
                if (foundFeedback != null) {
                    singleFeedbackDisplayArea.setText(
                            "ID: " + foundFeedback.getId() + "\n" +
                                    "Nome: " + foundFeedback.getNome() + "\n" +
                                    "Departamento: " + foundFeedback.getDepartamento() + "\n" +
                                    "Data/Hora: " + foundFeedback.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
                                    "Feedback: " + foundFeedback.getTextoFeedback()
                    );
                } else {
                    singleFeedbackDisplayArea.setText("Nenhum feedback encontrado com ID " + id + " para este usuário.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um ID numérico válido para a busca.", "Erro de ID", JOptionPane.ERROR_MESSAGE);
                singleFeedbackDisplayArea.setText("");
            }
        });

        // --- Organizar Painéis no UserPanel ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sendPanel, viewActionsPanel);
        splitPane.setResizeWeight(0.3); // sendPanel ocupa 30% da altura inicial
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Define a mensagem de boas-vindas.
     * @param message A mensagem a ser exibida.
     */
    public void setWelcomeMessage(String message) {
        welcomeLabel.setText(message);
    }

    /**
     * Carrega e exibe os feedbacks do usuário logado na tabela.
     */
    public void loadUserFeedbacks() {
        tableModel.setRowCount(0); // Limpa a tabela
        singleFeedbackDisplayArea.setText(""); // Limpa a área de feedback único
        List<Feedback> feedbacks = mainFrame.listarMeusFeedbacks();
        if (feedbacks != null && !feedbacks.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (Feedback f : feedbacks) {
                tableModel.addRow(new Object[]{
                        f.getId(),
                        f.getNome(),
                        f.getDepartamento(),
                        f.getTextoFeedback(),
                        f.getDataHora().format(formatter)
                });
            }
        } else {
            tableModel.addRow(new Object[]{"", "", "", "Nenhum feedback registrado.", ""});
        }
    }
}
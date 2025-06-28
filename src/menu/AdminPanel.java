package menu;

import entity.Feedback;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.format.DateTimeFormatter;


 // Painel para a interface de administrador.

public class AdminPanel extends JPanel {

    private SistemaFeedback mainFrame; // Referência para o JFrame principal
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JTextField searchField; // Busca por termo
    private JTextField searchIdField; // Busca por ID
    private JTextField deleteIdField; // Campo para ID ao deletar

    public AdminPanel(SistemaFeedback mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Visão Geral do Administrador");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Painel de Tabela de Feedbacks
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Todos os Feedbacks"));

        String[] columnNames = {"ID", "Nome", "Departamento", "Feedback", "Data/Hora"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células da tabela não editáveis
            }
        };
        feedbackTable = new JTable(tableModel);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de Controle (Busca, Deletar, Exportar, Logout)
        JPanel controlWrapperPanel = new JPanel(); // Wrapper para melhor organização
        controlWrapperPanel.setLayout(new BoxLayout(controlWrapperPanel, BoxLayout.Y_AXIS)); // Organiza verticalmente

        // Primeira linha de busca (Termo e Recarregar)
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchRow.add(new JLabel("Buscar Termo:"));
        searchField = new JTextField(15);
        searchRow.add(searchField);
        JButton searchButton = new JButton("Buscar por Termo");
        searchRow.add(searchButton);
        JButton refreshButton = new JButton("Recarregar Todos");
        searchRow.add(refreshButton);
        controlWrapperPanel.add(searchRow);

        // Segunda linha de busca (ID)
        JPanel searchIdRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchIdRow.add(new JLabel("Buscar ID:"));
        searchIdField = new JTextField(5);
        searchIdRow.add(searchIdField);
        JButton searchByIdButton = new JButton("Buscar por ID");
        searchIdRow.add(searchByIdButton);
        controlWrapperPanel.add(searchIdRow);

        // Terceira linha de ações (Excluir e Exportar)
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        actionRow.add(new JLabel("Excluir ID:"));
        deleteIdField = new JTextField(5);
        actionRow.add(deleteIdField);
        JButton deleteButton = new JButton("Excluir por ID");
        actionRow.add(deleteButton);
        JButton exportButton = new JButton("Exportar para Arquivo");
        actionRow.add(exportButton);
        controlWrapperPanel.add(actionRow);

        // Quarta linha (Logout)
        JPanel logoutRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Alinha o logout à direita
        JButton logoutButton = new JButton("Logout");
        logoutRow.add(logoutButton);
        controlWrapperPanel.add(logoutRow);


        tablePanel.add(controlWrapperPanel, BorderLayout.SOUTH); // Adiciona o wrapper ao painel principal

        add(tablePanel, BorderLayout.CENTER);

        // Adicionar Listeners
        feedbackTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && feedbackTable.getSelectedRow() != -1) {
                int selectedRow = feedbackTable.getSelectedRow();
                Object id = tableModel.getValueAt(selectedRow, 0); // O ID está na primeira coluna
                deleteIdField.setText(id.toString());
                searchIdField.setText(id.toString()); // Preenche o campo de busca por ID
            }
        });

        searchButton.addActionListener(e -> {
            String termo = searchField.getText().trim();
            if (termo.isEmpty()) {
                loadAllFeedbacks(); // Se vazio, recarrega tudo
            } else {
                searchFeedbacks(termo);
            }
        });

        searchByIdButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(searchIdField.getText());
                searchFeedbackById(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um ID numérico válido para a busca.", "Erro de ID", JOptionPane.ERROR_MESSAGE);
                loadAllFeedbacks(); // Volta a mostrar todos os feedbacks se o ID for inválido
            }
        });

        refreshButton.addActionListener(e -> loadAllFeedbacks());

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                mainFrame.deletarFeedbackAdmin(id);
                loadAllFeedbacks();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um ID válido para excluir.", "Erro de ID", JOptionPane.ERROR_MESSAGE);
            }
        });

        exportButton.addActionListener(e -> {
            String nomeArquivo = JOptionPane.showInputDialog(this, "Digite o nome do arquivo para exportar (ex: feedbacks.txt):", "Exportar Feedbacks", JOptionPane.PLAIN_MESSAGE);
            if (nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {
                mainFrame.exportarFeedbacksAdmin(nomeArquivo.trim());
            } else if (nomeArquivo != null) {
                JOptionPane.showMessageDialog(this, "Nome do arquivo não pode ser vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> mainFrame.logout());
    }


     // Carrega e exibe todos os feedbacks na tabela.

    public void loadAllFeedbacks() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Feedback> feedbacks = mainFrame.listarTodosFeedbacks();
        populateTable(feedbacks);
    }

    /**
     * Busca e exibe feedbacks com base em um termo.
     * @param termoBusca O termo para buscar em nome, departamento ou texto.
     */
    public void searchFeedbacks(String termoBusca) {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Feedback> resultados = mainFrame.buscarFeedbacksAdminPorCriterio(termoBusca);
        populateTable(resultados);
    }


     // Busca e exibe um feedback específico por ID.

    public void searchFeedbackById(int id) {
        tableModel.setRowCount(0); // Limpa a tabela
        Feedback feedback = mainFrame.buscarFeedbackAdminPorId(id);
        if (feedback != null) {
            populateTable(java.util.Collections.singletonList(feedback)); // Adiciona apenas o feedback encontrado
        } else {
            tableModel.addRow(new Object[]{"", "", "", "Nenhum feedback encontrado com ID " + id + ".", ""});
        }
    }

    // Metodo auxiliar para popular a tabela
    private void populateTable(List<Feedback> feedbacks) {
        tableModel.setRowCount(0); // Limpa a tabela antes de adicionar novos dados
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
            // Mensagem se a lista estiver vazia (aplica-se a todos, busca ou listar)
            tableModel.addRow(new Object[]{"", "", "", "Nenhum feedback disponível ou encontrado.", ""});
        }
    }
}
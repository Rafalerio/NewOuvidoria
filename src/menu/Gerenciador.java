package menu;

import dao.FeedbackDAO;
import dao.UsuarioDAO;
import java.time.LocalDateTime; // Adicionado para exportar data e hora
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter; // Adicionado para exportação de arquivo
import java.io.IOException; // Adicionado para tratamento de exceção de I/O
import java.io.PrintWriter; // Adicionado para escrita formatada em arquivo

public class Gerenciador {
    private FeedbackDAO feedbackDAO;

    public Gerenciador() {
        this.feedbackDAO = new FeedbackDAO();
    }

    public void registrarFeedback(String nome, String departamento, String textoFeedback) {
        // ID inicial 0, será atualizado pelo DAO após ser gerado pelo banco de dados
        Feedback feedback = new Feedback(0, nome, departamento, textoFeedback);
        feedbackDAO.salvarFeedback(feedback);
    }

    public List<Feedback> listarFeedback() {
        return feedbackDAO.listarTodosFeedbacks();
    }

    /**
     * Busca um feedback pelo seu ID no banco de dados.
     * @param id O ID do feedback a ser buscado.
     * @return O objeto Feedback encontrado, ou null se não for encontrado.
     */
    public Feedback buscarFeedbackPorId(int id) {
        return feedbackDAO.buscarFeedbackPorId(id);
    }

    /**
     * Atualiza o texto de um feedback existente no banco de dados.
     * @param id O ID do feedback a ser atualizado.
     * @param novoTexto O novo texto do feedback.
     */
    public void atualizarFeedback(int id, String novoTexto) {
        // O DAO retorna true/false indicando sucesso
        boolean sucesso = feedbackDAO.atualizarFeedback(id, novoTexto);
        if (sucesso) {
            System.out.println("Feedback com ID " + id + " atualizado com sucesso.");
        } else {
            System.out.println("Feedback com ID " + id + " não encontrado ou erro ao atualizar.");
        }
    }


    /**
     * Deleta um feedback do banco de dados.
     * @param id O ID do feedback a ser deletado.
     */
    public void deletarFeedback(int id) {
        // O DAO retorna true/false indicando sucesso
        boolean sucesso = feedbackDAO.deletarFeedback(id);
        if (sucesso) {
            System.out.println("Feedback com ID " + id + " removido com sucesso.");
        } else {
            System.out.println("Feedback com ID " + id + " não encontrado ou erro ao deletar.");
        }
    }

    /**
     * Exporta todos os feedbacks (obtidos do banco de dados) para um arquivo de texto.
     * @param nomeArquivo O nome do arquivo para o qual os feedbacks serão exportados.
     */
    public void exportarFeedbackParaArquivo(String nomeArquivo) throws IOException {
        List<Feedback> feedbacks = feedbackDAO.listarTodosFeedbacks(); // Obtém os feedbacks do DB em ordem inversa

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("--- Relatório de Feedbacks ---");
            writer.println("Data de Exportação: " + LocalDateTime.now());
            writer.println("-----------------------------");
            if (feedbacks.isEmpty()) {
                writer.println("Nenhum feedback registrado.");
            } else {
                for (Feedback feedback : feedbacks) {
                    writer.println(feedback.toString());
                }
            }
            writer.println("-----------------------------");
        }
    }
}
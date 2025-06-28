package dao;

import conexao.Conexao;
import entity.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    /**
     * Salva um novo feedback no banco de dados.
     * O ID será gerado automaticamente pelo banco.
     */

    public void salvarFeedback(Feedback feedback) {
        String sql = "INSERT INTO FEEDBACKS (nome_usuario, departamento_usuario, texto_feedback, data_hora) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Conexao.getConexao();
            // Retorna as chaves geradas automaticamente (como o ID)
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, feedback.getNome());
            ps.setString(2, feedback.getDepartamento());
            ps.setString(3, feedback.getTextoFeedback());
            ps.setObject(4, feedback.getDataHora()); // LocalDateTime é mapeado pelo driver JDBC para DATETIME

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Recupera o ID gerado pelo banco e seta no objeto Feedback
                        feedback.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Feedback salvo no banco de dados com ID: " + feedback.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar feedback no banco de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Não fecha a conexão aqui, pois ela é gerenciada globalmente pelo Conexao.getConexao()
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Lista todos os feedbacks do banco de dados em ordem cronológica inversa (mais recente primeiro).
     * @return Uma lista de objetos Feedback.
     */

    public List<Feedback> listarTodosFeedbacks() {
        String sql = "SELECT id, nome_usuario, departamento_usuario, texto_feedback, data_hora FROM FEEDBACKS ORDER BY data_hora DESC";
        List<Feedback> feedbacks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome_usuario");
                String departamento = rs.getString("departamento_usuario");
                String texto = rs.getString("texto_feedback");
                LocalDateTime dataHora = rs.getObject("data_hora", LocalDateTime.class); // Mapeia para LocalDateTime

                // Usa o novo construtor para popular o objeto Feedback com a data/hora do banco
                Feedback feedback = new Feedback(id, nome, departamento, texto, dataHora);
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar feedbacks do banco de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return feedbacks;
    }


    // Busca um feedback pelo seu ID no banco de dados.

    public Feedback buscarFeedbackPorId(int id) {
        String sql = "SELECT id, nome_usuario, departamento_usuario, texto_feedback, data_hora FROM FEEDBACKS WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Feedback feedback = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_usuario");
                String departamento = rs.getString("departamento_usuario");
                String texto = rs.getString("texto_feedback");
                LocalDateTime dataHora = rs.getObject("data_hora", LocalDateTime.class);
                feedback = new Feedback(id, nome, departamento, texto, dataHora); // Usa o novo construtor
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar feedback por ID no banco de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return feedback;
    }

    /**
     * Atualiza o texto de um feedback existente no banco de dados.
     * @param novoTexto O novo texto do feedback.
     */

    public boolean atualizarFeedback(int id, String novoTexto) {
        String sql = "UPDATE FEEDBACKS SET texto_feedback = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setString(1, novoTexto);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se alguma linha foi afetada

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar feedback no banco de dados: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
        }
    }


    // Deleta um feedback do banco de dados.

    public boolean deletarFeedback(int id) {
        String sql = "DELETE FROM FEEDBACKS WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se alguma linha foi afetada

        } catch (SQLException e) {
            System.err.println("Erro ao deletar feedback do banco de dados: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Lista feedbacks do banco de dados com base em um termo de busca,
     * pesquisando nos campos nome_usuario, departamento_usuario e texto_feedback.
     * @return Uma lista de objetos Feedback que correspondem ao termo, ordenada por data.
     */

    public List<Feedback> listarFeedbacksPorCriterio(String termoBusca) {
        String sql = "SELECT id, nome_usuario, departamento_usuario, texto_feedback, data_hora FROM FEEDBACKS " +
                "WHERE LOWER(nome_usuario) LIKE ? OR LOWER(departamento_usuario) LIKE ? OR LOWER(texto_feedback) LIKE ? " +
                "ORDER BY data_hora DESC";
        List<Feedback> feedbacks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConexao();
            ps = conn.prepareStatement(sql);
            // Adiciona wildcards e converte para minúsculas para busca case-insensitive
            String termoComWildcard = "%" + termoBusca.toLowerCase() + "%";
            ps.setString(1, termoComWildcard);
            ps.setString(2, termoComWildcard);
            ps.setString(3, termoComWildcard);

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome_usuario");
                String departamento = rs.getString("departamento_usuario");
                String texto = rs.getString("texto_feedback");
                LocalDateTime dataHora = rs.getObject("data_hora", LocalDateTime.class);

                Feedback feedback = new Feedback(id, nome, departamento, texto, dataHora);
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar feedbacks por critério: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return feedbacks;
    }
}
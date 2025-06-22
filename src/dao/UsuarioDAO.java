package dao;

import entity.Usuario;
import entity.Admin; // Importar a classe Admin
import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Importar ResultSet
import java.sql.SQLException;

/**
 * DAO = Data Access Object, essa classe vai ter métodos que fazem conexão com Banco de Dados
 */

public class UsuarioDAO {

    /**
     * Método de inserção de dados para cadastrar um novo usuário.
     */
    public void cadastrarUsuario(Usuario usuario) {
        // Ajuste no SQL: o campo EMAIL e FUNCAO estavam trocados nas posições (3 e 4)
        String sql = "INSERT INTO USUARIO (NOME, LOGIN, EMAIL, SENHA, FUNCAO, DEPARTAMENTO) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getEmail()); // Corrigido
            ps.setString(4, usuario.getSenha());
            ps.setString(5, usuario.getFuncao()); // Corrigido
            ps.setString(6, usuario.getDepartamento());

            ps.execute();
            System.out.println("Usuário " + usuario.getUsuario() + " cadastrado com sucesso no banco de dados.");
        } catch (SQLException e){
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica se um nome de usuário já existe no banco de dados.
     * @param nomeUsuario O nome de usuário a ser verificado.
     * @return true se o usuário já existe, false caso contrário.
     */
    public boolean verificarUsuarioExistente(String nomeUsuario) {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE LOGIN = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, nomeUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Se a contagem for maior que 0, o usuário existe.
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar usuário existente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Realiza o login de um usuário comum.
     * @param usuario Objeto Usuario contendo login e senha para autenticação.
     * @return O objeto Usuario completo se o login for bem-sucedido, null caso contrário.
     */
    public Usuario loginUsuario(Usuario usuario) {
        String sql = "SELECT NOME, LOGIN, EMAIL, FUNCAO, DEPARTAMENTO FROM USUARIO WHERE LOGIN = ? AND SENHA = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getSenha());
            rs = ps.executeQuery();

            if (rs.next()) {
                // Se encontrou, preenche o objeto Usuario com os dados do banco
                Usuario loggedInUser = new Usuario();
                loggedInUser.setNome(rs.getString("NOME"));
                loggedInUser.setUsuario(rs.getString("LOGIN"));
                loggedInUser.setEmail(rs.getString("EMAIL"));
                loggedInUser.setFuncao(rs.getString("FUNCAO"));
                loggedInUser.setDepartamento(rs.getString("DEPARTAMENTO"));
                return loggedInUser; // Retorna o objeto Usuario com os dados completos
            }
        } catch (SQLException e) {
            System.err.println("Erro ao realizar login de usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return null; // Login falhou
    }

    /**
     * Realiza o login de um administrador.
     * @param admin Objeto Admin contendo login e senha para autenticação.
     * @return true se o login do administrador for bem-sucedido, false caso contrário.
     */
    public boolean loginAdmin(Admin admin) {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE LOGIN = ? AND SENHA = ? AND FUNCAO = 'administrador'";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, admin.getUsuario());
            ps.setString(2, admin.getSenha());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Se a contagem for maior que 0, o login é válido.
            }
        } catch (SQLException e) {
            System.err.println("Erro ao realizar login de administrador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos (ResultSet/PreparedStatement): " + e.getMessage());
            }
        }
        return false;
    }
}
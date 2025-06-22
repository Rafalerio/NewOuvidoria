package dao;

import entity.Usuario;
import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO = Data Acess Object, essa classe vão ter métodos que fazem conexão com Banco de Dados
 */

public class UsuarioDAO {

    /**
     * Método de inserção de dados
     */

    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (NOME, LOGIN, EMAIL, SENHA, FUNCAO, DEPARTAMENTO) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getFuncao());
            ps.setString(6, usuario.getDepartamento());

            ps.execute();
            ps.close();
        } catch (SQLException e){

            e.printStackTrace();
        }
    }
}

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
            ps = Conexao.getConexao().preparedStatement(sql);
            ps.setString(parameterIndex:1, usuario.getNome());
            ps.setString(parameterIndex:2, usuario.getUsuario());
            ps.setString(parameterIndex:3, usuario.getSenha());
            ps.setString(parameterIndex:4, usuario.getEmail());
            ps.setString(parameterIndex:5, usuario.getFuncao());
            ps.setString(parameterIndex:6, usuario.getDepartamento());

            ps.execute();
            ps.close();
        } catch (SQLException e){

            e.printStackTrace();
        }
    }
}

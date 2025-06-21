package entity;

import dao.UsuarioDAO;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) throws Exception {

        Usuario u = new Usuario();
        u.setNome("Rafael");
        u.setUsuario("rafael");
        u.setSenha("123456");
        u.setEmail("rafa@gmail.com");
        u.setFuncao("CEO");
        u.setDepartamento("TI");

        new UsuarioDAO().cadastrarUsuario(u);
    }
}

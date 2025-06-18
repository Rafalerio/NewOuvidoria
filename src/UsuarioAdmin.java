public class Usuario {
    private String usuario;
    private String senha;

    public Usuario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    // Métodos getters
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
}

public class Admin extends Usuario {
    public Admin(String usuario, String senha) {
        super(usuario, senha);
    }
}

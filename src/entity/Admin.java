package entity;

public class Admin {
    private String usuario;
    private String senha;

    public Admin(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    // Métodos getters
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
}
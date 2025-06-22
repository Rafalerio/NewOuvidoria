package entity;

public class Usuario {
    private int codigo;
    private String nome;
    private String usuario; // Nome de usuário para login
    private String email;
    private String senha;
    private String funcao;
    private String departamento;

    // Construtor adicionado para facilitar a criação de objetos Usuario a partir do banco de dados ou para login
    public Usuario() {
        // Construtor padrão
    }

    // Construtor para cadastro com todos os campos (exceto código, que é gerado pelo banco)
    public Usuario(String nome, String usuario, String email, String senha, String funcao, String departamento) {
        this.nome = nome;
        this.usuario = usuario;
        this.email = email;
        this.senha = senha;
        this.funcao = funcao;
        this.departamento = departamento;
    }

    // Construtor para login, focando em usuário e senha
    public Usuario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    /**
     * Encapsulamento
     */

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getFuncao(){
        return funcao;
    }

    public void setFuncao(String funcao){
        this.funcao = funcao;
    }

    public String getDepartamento(){
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
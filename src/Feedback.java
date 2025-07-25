import java.time.LocalDateTime;

public class Feedback {
    private int id;
    private String nome;
    private String departamento;
    private String textoFeedback;
    private LocalDateTime dataHora;

    public Feedback(int id, String nome, String departamento, String textoFeedback) {
        this.id = id;
        this.nome = nome;
        this.departamento = departamento;
        this.textoFeedback = textoFeedback;
        this.dataHora = LocalDateTime.now();
    }

    // Métodos getters e toString
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDepartamento() { return departamento; }
    public String getTextoFeedback() { return textoFeedback; }
    public LocalDateTime getDataHora() { return dataHora; }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Departamento: " + departamento +
                ", Feedback: " + textoFeedback + ", Data/Hora: " + dataHora;
    }
}

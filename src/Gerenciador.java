import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Gerenciador {
    private Stack<Feedback> pilhaFeedback = new Stack<>();
    private List<Feedback> listaFeedback = new ArrayList<>();
    private int contadorFeedback = 1;

    public void registrarFeedback(String nome, String departamento, String textoFeedback) {
        Feedback feedback = new Feedback(contadorFeedback++, nome, departamento, textoFeedback);
        pilhaFeedback.push(feedback);
        listaFeedback.add(feedback);
    }

    public List<Feedback> listarFeedback() {
        List<Feedback> listaReversa = new ArrayList<>(pilhaFeedback);
        return listaReversa;
    }

    public Feedback buscarFeedbackPorId(int id) {
        for (Feedback feedback : listaFeedback) {
            if (feedback.getId() == id) {
                return feedback;
            }
        }
        return null;
    }

    public void atualizarFeedback(int id, String novoTextoFeedback) {
        Feedback feedback = buscarFeedbackPorId(id);
        if (feedback != null) {
            listaFeedback.remove(feedback);
            listaFeedback.add(new Feedback(feedback.getId(), feedback.getNome(), feedback.getDepartamento(), novoTextoFeedback));
        }
    }

    public void deletarFeedback(int id) {
        Feedback feedback = buscarFeedbackPorId(id);
        if (feedback != null) {
            listaFeedback.remove(feedback);
        }
    }

    public void exportarFeedbackParaArquivo(String nomeArquivo) {
        // Implementar lógica de exportação para arquivo aqui
    }
}

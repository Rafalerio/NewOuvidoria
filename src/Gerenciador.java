// No Gerenciador.java, dentro do método exportarFeedbackParaArquivo
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// ...

public void exportarFeedbackParaArquivo(String nomeArquivo) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
        writer.println("--- Relatório de Feedbacks ---");
        writer.println("Data de Exportação: " + LocalDateTime.now());
        writer.println("-----------------------------");
        if (listaFeedback.isEmpty()) {
            writer.println("Nenhum feedback registrado.");
        } else {
            // Exporta em ordem cronológica (a ordem da listaFeedback)
            for (Feedback feedback : listaFeedback) {
                writer.println(feedback.toString());
            }
        }
        writer.println("-----------------------------");
        System.out.println("Feedbacks exportados para " + nomeArquivo);
    }
}
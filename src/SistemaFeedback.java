import java.util.Scanner;

public class SistemaFeedback {
    private static GerenciadorFeedback gerenciadorFeedback = new GerenciadorFeedback();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Feedback");
        System.out.println("Escolha seu papel: 1. Usuário 2. Administrador");
        int papel = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        if (papel == 1) {
            menuUsuario();
        } else if (papel == 2) {
            menuAdmin();
        } else {
            System.out.println("Papel selecionado inválido.");
        }
    }

    private static void menuUsuario() {
        while (true) {
            System.out.println("1. Enviar Feedback");
            System.out.println("2. Sair");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            if (escolha == 1) {
                System.out.print("Digite seu nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite seu departamento: ");
                String departamento = scanner.nextLine();
                System.out.print("Digite seu feedback: ");
                String textoFeedback = scanner.nextLine();
                gerenciadorFeedback.registrarFeedback(nome, departamento, textoFeedback);
                System.out.println("Feedback enviado com sucesso.");
            } else {
                break;
            }
        }
    }

    private static void menuAdmin() {
        // Implementar funcionalidades do admin aqui
    }
}

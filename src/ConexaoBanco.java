import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CogitnexaoBanco {
    private static final String URL = "jdbc:mysql://localhost:3306/banco_feedback";
    private static final String USUARIO = "usuario";
    private static final String SENHA = "senha";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}

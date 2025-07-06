package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 // Conexao do banco de dados

public class Conexao {
    private static final String url = "";
    private static final String user = "";
    private static final String password = "";

    private static Connection con;

     // Verificando se a conexão ja foi feita e se ainda está aberta, caso não esteja, cria uma nova conexão.

    public static Connection getConexao(){
        try {
            // É uma boa prática carregar o driver explicitamente para garantir que ele esteja disponível.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Verifica se a conexão é nula ou se está fechada, para reestabelecer se necessário.
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, user, password);
                System.out.println("Conexão com o banco de dados Aiven estabelecida/reestabelecida.");
            }
            return con;

        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado. Verifique se o JAR está no classpath (mysql-connector-java.jar).");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados Aiven: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
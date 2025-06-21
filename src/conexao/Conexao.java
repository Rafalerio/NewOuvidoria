package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexao do banco de dados
 */

public class Conexao {
    private static final String url = "jdbc:mysql://localhost:3306/newouvidoria";
    private static final String user = "Rafalerio";
    private static final String password = "Hex304.gs";

    private static Connection con;

    /**
     * Verificando se a conexão ja foi feita, caso não tenha sido, cria uma conexão
     */

    public static Connection getConexao(){
        try {
            if (con == null) {
                con = DriverManager.getConnection(url, user, password);
                return con;
            }else{
                return con;
            }

            /**
             * Caso não for possivel se conectar e dê erro, ele retorna nulo
             */

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

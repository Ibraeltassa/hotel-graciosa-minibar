package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    static String url = "jdbc:sqlite:src/main/java/database/minibar.db";

    // Metodo para conectar ao bando de dados
    public static Connection connect() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexão com SQLite feita com secesso");

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao SQLite: " + e.getMessage());
        }

        return connection;

    }

    // Metodo para fechar a conexão
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada.");
            }catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    // Testando a conexão
    public static void main(String[] args) {
        Connection connection = connect(); //Abre conexão
        close(connection); //Fecha conexão
    }

}

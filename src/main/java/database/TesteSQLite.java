package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteSQLite {
    public static void connect(){
        String url = "jdbc:sqlite:src/main/java/database/minibar.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                System.out.println("Conexão com SQLite feita com secesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao SQLite: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        connect();
    }

}

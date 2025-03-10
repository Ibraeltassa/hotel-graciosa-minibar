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


    public static void criarTabelas() {
        String sqlQuartos = "CREATE TABLE IF NOT EXISTS quartos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "preco REAL NOT NULL"
                + ");";

        String sqlProdutos = "CREATE TABLE IF NOT EXISTS produtos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "preco REAL NOT NULL"
                + ");";

        String sqlConsumo = "CREATE TABLE IF NOT EXISTS consumos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "quarto_id INTEGER NOT NULL,"
                + "produto_id INTEGER NOT NULL,"
                + "data TEXT NOT NULL,"
                + "FOREIGN KEY(quarto_id) REFERENCES quartos(id),"
                + "FOREIGN KEY(produto_id) REFERENCES produtos(id)"
                + ");";


        try (Connection connection = connect();

            java.sql.Statement statement = connection.createStatement()) {
            statement.execute(sqlQuartos);
            statement.execute(sqlProdutos);
            statement.execute(sqlConsumo);
            System.out.println("Tabelas criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar Tabela: " + e.getMessage());
        }

    }


    public static void inserirDadosIniciais() {
        String sqlInsertQuartos = "INSERT INTO quartos (nome, preco) SELECT 'Quarto 202', 150.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM quartos WHERE nome = 'Quarto 202');";

        String sqlInsertProdutos = "INSERT INTO produtos (nome, preco) SELECT 'Água Mineral', 5.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Água Mineral');";

        try (Connection connection = connect();
            java.sql.Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlInsertQuartos);
            statement.executeUpdate(sqlInsertProdutos);

            System.out.println("Dados iniciais inseridos com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir dados inicias: " + e.getMessage());
        }

    }



    // Testando a conexão
    public static void main(String[] args) {
        Connection connection = connect(); //Abre conexão
        criarTabelas(); // Cria tabelas caso não existam
        inserirDadosIniciais(); //Insere dados iniciais
        close(connection); //Fecha conexão
    }

}

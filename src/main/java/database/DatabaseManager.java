package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public static void registrarConsumo(int quarto_id, int produto_id, String data) {

        //Converter a data para o formato -> dd/MM/yyyy
        DateTimeFormatter formatoBrasil = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataFormatada = LocalDate.parse(data, formatoBrasil);

        String sql = "INSERT INTO consumos (quarto_id, produto_id, data) VALUES (?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, quarto_id);
            preparedStatement.setInt(2, produto_id);
            preparedStatement.setString(3, data);

            preparedStatement.executeUpdate();
            System.out.println("Consumo registrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao registrar consumo: " + e.getMessage());
        }
    }


    public static void visualizarConsumos() {
        String sql = "SELECT consumo.id, quarto.nome AS quarto, produto.nome AS produto, consumo.data AS data " +
                "FROM consumos consumo " +
                "JOIN quartos quarto ON consumo.quarto_id = quarto.id " +
                "JOIN produtos produto ON consumo.produto_id = produto.id;";

        try (Connection connection = connect();
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\n\uD83D\uDCCC Registros de Consumo: ");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Quarto: " + resultSet.getString("quarto") +
                        ", Produto: " + resultSet.getString("produto") +
                        ", Data: " + resultSet.getString("data"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao visualizar consumos: " + e.getMessage());
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


    public static void visualizarDados() {
        String sqlQuartos = "SELECT * FROM quartos;";
        String sqlProdutos = "SELECT * FROM produtos;";

        try(Connection connection = connect();
            java.sql.Statement statement = connection.createStatement()) {

            System.out.println("\n\uD83D\uDCCC Quartos cadastrados: ");
            java.sql.ResultSet resultSetQuartos = statement.executeQuery(sqlQuartos);
            while(resultSetQuartos.next()) {
                System.out.println("ID: " + resultSetQuartos.getInt("id") +
                        ", Nome: " + resultSetQuartos.getString("nome") +
                        ", Preço: " + resultSetQuartos.getDouble("preco"));
            }

            System.out.println("\n\uD83D\uDCCC Produtos cadastrados: ");
            java.sql.ResultSet resultSetProdutos = statement.executeQuery(sqlProdutos);
            while(resultSetProdutos.next()) {
                System.out.println("ID: " + resultSetQuartos.getInt("id") +
                        ", Nome: " + resultSetQuartos.getString("nome") +
                        ", Preço: " + resultSetQuartos.getDouble("preco"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao visualizar dados: " + e.getMessage());
        }

    }

    // Testando a conexão
    public static void main(String[] args) {
        Connection connection = connect(); //Abre conexão
        criarTabelas(); // Cria tabelas caso não existam
        registrarConsumo(1,1, "12/03/2024");
        visualizarConsumos(); // Visualiza consumo gerado
        inserirDadosIniciais(); //Insere dados iniciais
        visualizarDados(); // Lista os dados criados
        close(connection); //Fecha conexão
    }

}

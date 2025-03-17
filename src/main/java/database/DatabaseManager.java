package database;

import java.sql.*;
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
                + "quantidade INTERGER NOT NULL DEFAULT 1,"
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

    public static void registrarConsumo(int quarto_id, int produto_id, int quantidade, String data) {

        //Converter a data para o formato -> dd/MM/yyyy
        DateTimeFormatter formatoBrasil = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataFormatada = LocalDate.parse(data, formatoBrasil);

        String sqlVerifica = "SELECT id, quantidade FROM consumos WHERE quarto_id = ? AND produto_id = ? AND data = ?";
        String sqlAtualiza = "UPDATE consumos SET quantidade = quantidade + ? WHERE id = ?";
        String sqlInsere = "INSERT INTO consumos (quarto_id, produto_id, quantidade, data) VALUES (?, ?, ?, ?)";

        try (Connection connection = connect()) {
            PreparedStatement statementVerifica = connection.prepareStatement(sqlVerifica);
            statementVerifica.setInt(1, quarto_id);
            statementVerifica.setInt(2, produto_id);
            statementVerifica.setString(3, data);
            ResultSet resultSet = statementVerifica.executeQuery();

            if (resultSet.next()) {
                // Se já existe um consumo registrado, apenas atualizamos a quantidade
                int idConsumo = resultSet.getInt("id");
                PreparedStatement statementAtualiza = connection.prepareStatement(sqlAtualiza);
                statementAtualiza.setInt(1, quarto_id);
                statementAtualiza.setInt(2, idConsumo);
                statementAtualiza.executeUpdate();
                System.out.println("Quantidade atualizada no consumo existente.");
            } else {
                // Se não existe consumo registrado, inserimos um novo
                PreparedStatement statementInsere = connection.prepareStatement(sqlInsere);
                statementInsere.setInt(1, quarto_id);
                statementInsere.setInt(2, produto_id);
                statementInsere.setInt(3, quantidade);
                statementInsere.setString(4, data);
                statementInsere.executeUpdate();
                System.out.println("Novo consumo registrado com sucesso!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao registrar consumo: " + e.getMessage());
        }
    }


    public static void visualizarConsumos() {
        String sql = "SELECT consumo.id, quarto.nome AS quarto, produto.nome AS produto, consumo.quantidade, consumo.data AS data " +
                "FROM consumos consumo " +
                "JOIN quartos quarto ON consumo.quarto_id = quarto.id " +
                "JOIN produtos produto ON consumo.produto_id = produto.id;";

        try (Connection connection = connect();
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\n\uD83D\uDCCC Registros de Consumo: ");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", NOME: " + resultSet.getString("quarto") +
                        ", PRODUTO: " + resultSet.getString("produto") +
                        ", QUANTIDADE: " + resultSet.getInt("quantidade") +
                        ", DATA: " + resultSet.getString("data"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao visualizar consumos: " + e.getMessage());
        }
    }

    public static void inserirDadosIniciais() {
        String sqlInsertQuartos = "INSERT INTO quartos (nome, preco) SELECT 'Quarto 202', 150.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM quartos WHERE nome = 'Quarto 202');" +
                "INSERT INTO quartos (nome, preco) SELECT 'Quarto 201', 200.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM quartos WHERE nome = 'Quarto 201');";

        String sqlInsertProdutos = "INSERT INTO produtos (nome, preco) SELECT 'Água Mineral', 5.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Água Mineral');" +
                "INSERT INTO produtos (nome, preco) SELECT 'Coca-Cola', 7.00 " +
                "WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE nome = 'Coca-Cola');";


        try (Connection connection = connect();
            java.sql.Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlInsertQuartos);
            statement.executeUpdate(sqlInsertProdutos);

            System.out.println("Dados iniciais inseridos com sucesso");

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
                System.out.println("ID: " + resultSetProdutos.getInt("id") +
                        ", Nome: " + resultSetProdutos.getString("nome") +
                        ", Preço: " + resultSetProdutos.getDouble("preco"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao visualizar dados: " + e.getMessage());
        }
    }

    public static void limparConsumos(){
        String sql = "DELETE FROM consumos";

        try(Connection connection = connect();
            java.sql.Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Todos os registros de consumo foram apagados.");

        } catch (Exception e) {
            System.out.println("Erro ao limpar consumos: " + e.getMessage());
        }
    }


    public static void deletarQuarto(String nomeQuarto) {
        String sql = "DELETE FROM quartos WHERE nome = ?";

        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nomeQuarto);
            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Quarto `" + nomeQuarto + "` removido com sucesso");
            } else {
                System.out.println("Nenhum quarto encontrado com o nome `" + nomeQuarto + "`");
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar quarto: " + e.getMessage());
        }
    }


    // Testando a conexão
    public static void main(String[] args) {
        Connection connection = connect(); //Abre conexão
        criarTabelas(); // Cria tabelas caso não existam
        registrarConsumo(1,1, 3,"12/03/2024");
        visualizarConsumos(); // Visualiza consumo gerado
        inserirDadosIniciais(); // Insere dados iniciais
        visualizarDados(); // Lista os dados criados
        close(connection); //Fecha conexão

        //limparConsumos(); // Apaga todos os consumos SÓ USAR QUANDO NECESSÁRIO
        //deletarQuarto("Q202"); // Deleta um quarto (passar nome do quarto dentro de ()) SÓ USAR QUANDO NECESSÁRIO
    }
}

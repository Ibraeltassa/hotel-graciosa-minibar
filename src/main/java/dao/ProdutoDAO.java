package dao;

import database.DatabaseManager;
import model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProdutoDAO {

    public void salvarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?,?)";

        try (Connection connection = DatabaseManager.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());

            statement.executeUpdate();
            System.out.println("✅ Produto salvo com sucesso: " + produto.getNome());

        } catch (Exception e) {
            System.out.println("❌ Erro ao salvar produto: " + e.getMessage());
        }
    }

}
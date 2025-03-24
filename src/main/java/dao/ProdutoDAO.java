package dao;

import database.DatabaseManager;
import model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void atualizarProduto(int id, String novoNome, double novoPreco) {
        String sql = "UPDATE produtos SET nome = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, novoNome);
            statement.setDouble(2, novoPreco);
            statement.setInt(3, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Produto atualizado com sucesso!");
            } else {
                System.out.println("❌ Produto não encontrado!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void excluirProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection connection = DatabaseManager.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Produto excluído com sucesso!");
            } else {
                System.out.println("❌ Produto não encontrado!");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao excluir produto: " + e.getMessage());
        }

    }

}
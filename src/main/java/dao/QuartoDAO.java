package dao;

import database.DatabaseManager;
import model.Quarto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuartoDAO {

    public void salvarQuarto(Quarto quarto) {
        String sql = "INSERT INTO quartos (nome, preco) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, quarto.getNome());
            statement.setDouble(2, quarto.getPreco());

            statement.executeUpdate();
            System.out.println("✅ Quarto cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao cadastrar quarto: " + e.getMessage());
        }
    }

    public void atualizarQuarto(int id, String novoNome, double novoPreco) {
        String sql = "UPDATE quartos SET nome = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, novoNome);
            statement.setDouble(2, novoPreco);
            statement.setInt(3, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Quarto atualizado com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum quarto foi encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar quarto: " + e.getMessage());
        }
    }

}

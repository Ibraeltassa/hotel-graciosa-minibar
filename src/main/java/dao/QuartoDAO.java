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
}

package service;

import dao.QuartoDAO;
import model.Quarto;

public class QuartoService {

    private QuartoDAO quartoDAO = new QuartoDAO();

    public void cadastrarQuarto(String nome, double preco) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("❌ Nome do quarto não pode ser vazio!");
            return;
        }
        if (preco <= 0) {
            System.out.println("❌ Preço do quarto deve ser maior que zero!");
            return;
        }
        Quarto quarto = new Quarto(nome, preco);
        quartoDAO.salvarQuarto(quarto);
    }
}

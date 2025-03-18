package service;

import dao.ProdutoDAO;
import model.Produto;

public class ProdutoService {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void cadastrarProduto(String nome, double preco) {
        Produto produto = new Produto(nome, preco);
        produtoDAO.salvarProduto(produto);
    }
}

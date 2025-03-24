import dao.ProdutoDAO;
import dao.QuartoDAO;
import service.ProdutoService;
import service.QuartoService;

public class Main {

    public static void main(String[] args) {

        ProdutoService produtoService = new ProdutoService();
        QuartoService quartoService = new QuartoService();

        //Testando inserção de produto
        //produtoService.cadastrarProduto("Suco de Laranja", 8.50);

        //Cadastrando um novo quarto
        //quartoService.cadastrarQuarto("Quarto 205", 250.0);

        QuartoDAO quartoDAO = new QuartoDAO();
        //quartoDAO.atualizarQuarto(1, "Quarto 202", 175.00);
        //quartoDAO.excluirQuarto(1);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        //produtoDAO.atualizarProduto(1,"Água Minral", 5.50);
        //produtoDAO.excluirProduto(2);

    }

}

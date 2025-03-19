import service.ProdutoService;
import service.QuartoService;

public class Main {

    public static void main(String[] args) {

        ProdutoService produtoService = new ProdutoService();
        QuartoService quartoService = new QuartoService();

        //Testando inserção de produto
        produtoService.cadastrarProduto("Suco de Laranja", 8.50);

        //Cadastrando um novo quarto
        quartoService.cadastrarQuarto("Quarto 205", 250.0);

    }

}

import service.ProdutoService;

public class Main {

    public static void main(String[] args) {

        ProdutoService produtoService = new ProdutoService();

        //Testando inserção de produto
        produtoService.cadastrarProduto("Suco de Laranja", 8.50);

    }

}

package br.com.starlog.main;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Inicializando o DAO
        ProdutoDAO dao = new ProdutoDAOSqlite();

        // ==========================================
        // 1. CREATE - CADASTRAR DOIS PRODUTOS
        // ==========================================

        System.out.println("===== CADASTRO DE PRODUTOS =====");

        Produto teclado = new Produto(
                "Teclado Mecânico RGB", 199.90, 10
        );

        Produto mousepad = new Produto(
                "Mousepad Speed XL", 59.90, 20
        );

        dao.salvar(teclado);
        dao.salvar(mousepad);

        System.out.println("Produto cadastrado: " + teclado);
        System.out.println("Produto cadastrado: " + mousepad);


        // ==========================================
        // 2. READ - LISTAR TODOS OS PRODUTOS
        // ==========================================

        System.out.println("\n===== LISTAGEM DE PRODUTOS =====");

        List<Produto> produtos = dao.listarTodos();

        for (Produto produto : produtos) {
            System.out.println(produto);
        }


        // ==========================================
        // 3. UPDATE - ATUALIZAR O TECLADO
        // ==========================================

        System.out.println("\n===== ATUALIZAÇÃO DE PRODUTO =====");

        teclado.setEstoque(15);
        teclado.setPreco(289.90);

        boolean atualizado = dao.atualizar(teclado);

        if (atualizado) {
            System.out.println("Produto atualizado com sucesso!");

            dao.buscarPorId(teclado.getId()).ifPresent(
                    System.out::println
            );

        } else {
            System.out.println("Produto não encontrado.");
        }


        // ==========================================
        // 4. DELETE - EXCLUIR O MOUSEPAD
        // ==========================================

        System.out.println("\n===== EXCLUSÃO DE PRODUTO =====");

        boolean excluido = dao.excluir(mousepad.getId());

        if (excluido) {
            System.out.println("Mousepad excluído com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }


        // Testando exclusão de ID inexistente

        boolean inexistente = dao.excluir(888L);

        if (!inexistente) {
            System.out.println(
                    "ID 888 não encontrado. Nenhum produto foi excluído."
            );
        }


        // ==========================================
        // 5. FINAL STATE - ESTADO FINAL DO BANCO
        // ==========================================

        System.out.println("\n===== ESTADO FINAL DO ESTOQUE =====");

        List<Produto> resultadoFinal = dao.listarTodos();

        for (Produto produto : resultadoFinal) {
            System.out.println(produto);
        }

        System.out.println(
                "\nTotal de produtos restantes: "
                + resultadoFinal.size()
        );
    }
}

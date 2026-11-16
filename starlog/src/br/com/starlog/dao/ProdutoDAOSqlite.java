package br.com.starlog.dao;

import br.com.starlog.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOSqlite implements ProdutoDAO {

    // Criação automática da tabela no banco
    public ProdutoDAOSqlite() {

        String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL,
                    estoque INTEGER NOT NULL
                );
                """;

        try (Connection conn = ConexaoFactory.obterConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao criar tabela: " + e.getMessage()
            );
        }
    }

    // CREATE: cadastrar produto
    @Override
    public void salvar(Produto produto) {

        String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());

            stmt.executeUpdate();

            // Recuperar o ID gerado automaticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao salvar produto: " + e.getMessage()
            );
        }
    }

    // READ: buscar produto pelo ID
    @Override
    public Optional<Produto> buscarPorId(Long id) {

        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar produto: " + e.getMessage()
            );
        }
    }

    // READ: listar todos os produtos em ordem alfabética
    @Override
    public List<Produto> listarTodos() {

        String sql = "SELECT id, nome, preco, estoque FROM produtos ORDER BY nome ASC";

        List<Produto> lista = new ArrayList<>();

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Produto produto = mapearLinha(rs);

                lista.add(produto);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar produtos: " + e.getMessage()
            );
        }
    }

    // UPDATE: atualizar produto pelo ID
    @Override
    public boolean atualizar(Produto produto) {

        String sql = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setLong(4, produto.getId());

            int afetadas = stmt.executeUpdate();

            return afetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao atualizar produto: " + e.getMessage()
            );
        }
    }

    // DELETE: excluir produto pelo ID
    @Override
    public boolean excluir(Long id) {

        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoFactory.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int afetadas = stmt.executeUpdate();

            return afetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao excluir produto: " + e.getMessage()
            );
        }
    }

    // Converter uma linha do banco em objeto Produto
    private Produto mapearLinha(ResultSet rs) throws SQLException {

        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("estoque")
        );
    }
}

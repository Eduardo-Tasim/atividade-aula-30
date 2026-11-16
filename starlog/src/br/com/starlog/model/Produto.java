package br.com.starlog.model;

public class Produto {

    private Long id;
    private String nome;
    private double preco;
    private int estoque;

    // Construtor para produtos que já possuem ID
    public Produto(Long id, String nome, double preco, int estoque) {
        this.id = id;
        setNome(nome);
        setPreco(preco);
        setEstoque(estoque);
    }

    // Construtor para novos produtos
    public Produto(String nome, double preco, int estoque) {
        setNome(nome);
        setPreco(preco);
        setEstoque(estoque);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException(
                "O nome deve possuir pelo menos 3 caracteres."
            );
        }

        this.nome = nome.trim();
    }

    public void setPreco(double preco) {
        if (preco <= 0.0) {
            throw new IllegalArgumentException(
                "O preço deve ser maior que zero."
            );
        }

        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException(
                "O estoque não pode ser negativo."
            );
        }

        this.estoque = estoque;
    }

    // Representação textual do produto
    @Override
    public String toString() {
        return "ID: " + id
                + " | Nome: " + nome
                + " | Preço: R$ " + String.format("%.2f", preco)
                + " | Estoque: " + estoque;
    }
}

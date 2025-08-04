package com.example.aulaspraticas.model;

import java.io.Serializable;

public class CarrinhoItem implements Serializable {
    private CardapioItem produto;
    private int quantidade;

    public CarrinhoItem(CardapioItem produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public CardapioItem getProduto() {
        return produto;
    }

    public void setProduto(CardapioItem produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        try {
            return Double.parseDouble(produto.getPreco()) * quantidade;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}

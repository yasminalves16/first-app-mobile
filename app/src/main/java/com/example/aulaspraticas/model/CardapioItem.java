package com.example.aulaspraticas.model;

public class CardapioItem {
    public int imagem;
    public String titulo;
    public String descricao;
    public String preco;
    public String tempo;
    public boolean disponivel;

    public CardapioItem(int imagem, String titulo, String descricao, String preco, String tempo, boolean disponivel) {
        this.imagem = imagem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.tempo = tempo;
        this.disponivel = disponivel;
    }
}

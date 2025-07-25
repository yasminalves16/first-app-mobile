package com.example.aulaspraticas.model;

public class CardapioItem {
    private int id;
    private String imagem;
    private String titulo;
    private String descricao;
    private String preco;
    private String tempo;
    private boolean disponivel;

    public CardapioItem(int id, String imagem, String titulo, String descricao, String preco, String tempo, boolean disponivel) {
        this.id = id;
        this.imagem = imagem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.tempo = tempo;
        this.disponivel = disponivel;
    }

    public int getId() {
        return id;
    }

    public String getImagem() {
        return imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPreco() {
        return preco;
    }

    public String getTempo() {
        return tempo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }
}


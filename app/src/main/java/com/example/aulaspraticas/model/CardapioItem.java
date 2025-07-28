package com.example.aulaspraticas.model;

import java.util.List;

public class CardapioItem {
    private int id;
    private String imagem;
    private String titulo;
    private String descricao;
    private String preco;
    private String tempo;
    private boolean disponivel;
    private boolean gluten;
    private String categoria;
    private List<String> ingredientes;
    private List<String> alergenicos;

    public CardapioItem(int id, String imagem, String titulo, String descricao, String preco, String tempo,
                        boolean disponivel, boolean gluten, String categoria,
                        List<String> ingredientes, List<String> alergenicos) {
        this.id = id;
        this.imagem = imagem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.tempo = tempo;
        this.disponivel = disponivel;
        this.gluten = gluten;
        this.categoria = categoria;
        this.ingredientes = ingredientes;
        this.alergenicos = alergenicos;
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

    public boolean isGluten() {
        return gluten;
    }

    public String getCategoria() {
        return categoria;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public List<String> getAlergenicos() {
        return alergenicos;
    }
}

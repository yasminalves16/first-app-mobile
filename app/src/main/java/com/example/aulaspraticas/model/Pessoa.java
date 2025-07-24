package com.example.aulaspraticas.model;

public class Pessoa {

    private String nome;

    private int idade;

    private String pet;

    private String cidade;

    private String email;
    private String telefone;

    public Pessoa(String nome, int idade, String pet, String cidade, String email, String telefone) {
        this.nome = nome;
        this.idade = idade;
        this.pet = pet;
        this.cidade = cidade;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getPet() {
        return pet;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

}

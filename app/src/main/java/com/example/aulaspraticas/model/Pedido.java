package com.example.aulaspraticas.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Date;


public class Pedido implements Serializable {

    private String id;
    private List<CarrinhoItem> itens;
    private double total;
    private String metodoPagamento;
    private Date dataDoPedido;
    private Endereco enderecoEntrega;

    public Pedido() {
    }

    public Pedido(List<CarrinhoItem> itens, double total, String metodoPagamento) {
        this.id = gerarIdCurto();
        this.itens = itens;
        this.total = total;
        this.metodoPagamento = metodoPagamento;
        this.dataDoPedido = new Date();
    }

    public Pedido(List<CarrinhoItem> itens, double total, String metodoPagamento, Endereco enderecoEntrega) {
        this.id = gerarIdCurto();
        this.itens = itens;
        this.total = total;
        this.metodoPagamento = metodoPagamento;
        this.dataDoPedido = new Date();
        this.enderecoEntrega = enderecoEntrega;
    }

    private String gerarIdCurto() {
        Random random = new Random();
        int numero = 100 + random.nextInt(900);
        return String.valueOf(numero);
    }

    public String getId() {
        return id;
    }

    public List<CarrinhoItem> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public Date getDataDoPedido() {
        return dataDoPedido;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItens(List<CarrinhoItem> itens) {
        this.itens = itens;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setDataDoPedido(Date dataDoPedido) {
        this.dataDoPedido = dataDoPedido;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}

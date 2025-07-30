package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PagamentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        Button botaoFinalizar = findViewById(R.id.button_finalizar_pedido);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PagamentoActivity.this, PedidosActivity.class);
                startActivity(intent);
                finish();
            }
        });

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PagamentoActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PagamentoActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PagamentoActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(PagamentoActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }
}

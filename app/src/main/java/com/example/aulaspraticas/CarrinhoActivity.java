package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CarrinhoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);
        Button botaoIrPagamento = findViewById(R.id.button_continuar_pagamento);

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });
        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, EnderecosActivity.class);
                intent.putExtra("isCheckoutFlow", true);
                startActivity(intent);

            }
        });



    }
}

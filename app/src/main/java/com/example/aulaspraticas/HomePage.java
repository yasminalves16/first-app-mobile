package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage);

        Button botaoIrCardapioAll = findViewById(R.id.botao_todos);
        Button botaoIrCardapioEntradas = findViewById(R.id.botao_entradas);
        Button botaoIrCardapioPratosPrincipais = findViewById(R.id.botao_pratosprincipais);
        Button botaoIrCardapioMassas = findViewById(R.id.botao_massas);
        Button botaoIrCardapioBebidas = findViewById(R.id.botao_bebidas);
        Button botaoIrCardapioSobremesas = findViewById(R.id.botao_sobremesas);

        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoIrCardapioAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapioEntradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapioPratosPrincipais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapioMassas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapioBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapioSobremesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Carrinho.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomePage.this, Perfil.class);
                startActivity(intent);
            }
        });
    }
}
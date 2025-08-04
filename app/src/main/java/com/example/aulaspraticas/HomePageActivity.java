package com.example.aulaspraticas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        Button botaoIrCardapioAll = findViewById(R.id.botao_todos);
        Button botaoIrCardapioEntradas = findViewById(R.id.botao_entradas);
        Button botaoIrCardapioPratosPrincipais = findViewById(R.id.botao_pratosprincipais);
        Button botaoIrCardapioMassas = findViewById(R.id.botao_massas);
        Button botaoIrCardapioBebidas = findViewById(R.id.botao_bebidas);
        Button botaoIrCardapioSobremesas = findViewById(R.id.botao_sobremesas);

        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        SharedPreferences preferences = getSharedPreferences("Restaurante.autenticacao", MODE_PRIVATE);
        boolean isLogged = preferences.getBoolean("isLogged", false);

        if (!isLogged) {
            Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        botaoIrCardapioAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Todos");
            startActivity(intent);
        });

        botaoIrCardapioEntradas.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Entradas");
            startActivity(intent);
        });

        botaoIrCardapioPratosPrincipais.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Pratos Principais");
            startActivity(intent);
        });

        botaoIrCardapioMassas.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Massas");
            startActivity(intent);
        });

        botaoIrCardapioBebidas.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Bebidas");
            startActivity(intent);
        });

        botaoIrCardapioSobremesas.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
            intent.putExtra("categoria", "Sobremesas");
            startActivity(intent);
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        FooterHelper.setupFooter(this);
    }
}
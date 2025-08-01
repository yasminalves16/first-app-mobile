package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class EnderecosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);


        boolean isCheckoutFlow = getIntent().getBooleanExtra("isCheckoutFlow", false);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        Button selecionarEndereco1 = findViewById(R.id.selecionar_botao_1);
        Button selecionarEndereco2 = findViewById(R.id.selecionar_botao_2);


        if (isCheckoutFlow) {
            selecionarEndereco1.setVisibility(View.VISIBLE);
            selecionarEndereco2.setVisibility(View.VISIBLE);

            selecionarEndereco1.setOnClickListener(v -> {
                Intent intent = new Intent(EnderecosActivity.this, PagamentoActivity.class);
                startActivity(intent);
                finish();
            });

            selecionarEndereco2.setOnClickListener(v -> {
                Intent intent = new Intent(EnderecosActivity.this, PagamentoActivity.class);
                startActivity(intent);
                finish();
            });

        } else {
            selecionarEndereco1.setVisibility(View.GONE);
            selecionarEndereco2.setVisibility(View.GONE);
        }

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EnderecosActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EnderecosActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EnderecosActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(EnderecosActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        FooterHelper.setupFooter(this);
    }
}
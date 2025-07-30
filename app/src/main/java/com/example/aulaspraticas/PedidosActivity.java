package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PedidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PedidosActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PedidosActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PedidosActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(PedidosActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

    }
}
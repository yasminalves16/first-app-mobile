package com.example.aulaspraticas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Perfil extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoLogout = findViewById(R.id.botao_logout);

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Perfil.this, HomePage.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Perfil.this, Cardapio.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Perfil.this, Carrinho.class);
                startActivity(intent);
            }
        });

        botaoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SharedPreferences preferences = getSharedPreferences("Restaurante.autenticacao", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLogged", false);
                editor.apply();

                Intent intent = new Intent(Perfil.this, Login.class);
                startActivity(intent);
            }
        });
    }
}

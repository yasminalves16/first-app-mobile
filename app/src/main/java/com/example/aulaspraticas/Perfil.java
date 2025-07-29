package com.example.aulaspraticas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        Button botaoEndereco = findViewById(R.id.botao_enderecos);
        Button botaoPedidos = findViewById(R.id.botao_ver_pedidos);


        TextView nomeUsuario = findViewById(R.id.nome_usuario);
        TextView emailUsuario = findViewById(R.id.email_usuario);
        TextView telefoneUsuario = findViewById(R.id.telefone_usuario);

        SharedPreferences preferences = getSharedPreferences("Restaurante.autenticacao", MODE_PRIVATE);
        String nome = preferences.getString("nomeUsuario", "Nome não encontrado");
        String email = preferences.getString("emailUsuario", "Email não encontrado");
        String telefone = preferences.getString("telefoneUsuario", "Telefone não encontrado");

        nomeUsuario.setText("Nome: " + nome);
        emailUsuario.setText("Email: " + email);
        telefoneUsuario.setText("Telefone: " + telefone);


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

        botaoEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Perfil.this, Enderecos.class);
                startActivity(intent);
            }
        });

        botaoPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, Pedidos.class);
                startActivity(intent);
            }
        });

    }
}

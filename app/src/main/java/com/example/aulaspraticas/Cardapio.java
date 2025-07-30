package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.adapter.CardapioAdapter;
import com.example.aulaspraticas.model.CardapioItem;
import com.example.aulaspraticas.data.remote.ItemCardapioApiClient;


import java.util.List;
import java.util.stream.Collectors;


public class Cardapio extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardapioAdapter adapter;
    private String categoriaSelecionada = "Todos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardapio);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Cardapio.this, HomePage.class);
                startActivity(intent);
            }
        });
        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Cardapio.this, Carrinho.class);
                startActivity(intent);
            }
        });
        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Cardapio.this, Perfil.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler_cardapio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("categoria")) {
            categoriaSelecionada = intent.getStringExtra("categoria");
        }

        listagemProdutos();

    }

    private void listagemProdutos() {
        ItemCardapioApiClient.getInstance(this).listagemProdutos(new ItemCardapioApiClient.ProdutosCallback() {
            @Override
            public void onSuccess(List<CardapioItem> produtos) {
                List<CardapioItem> produtosFiltrados;

                if (!categoriaSelecionada.equals("Todos")) {
                    produtosFiltrados = produtos.stream()
                            .filter(p -> p.getCategoria().equalsIgnoreCase(categoriaSelecionada))
                            .collect(Collectors.toList());
                } else {
                    produtosFiltrados = produtos;
                }

                adapter = new CardapioAdapter(Cardapio.this, produtosFiltrados);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(Cardapio.this, "Erro ao listar produtos: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

}

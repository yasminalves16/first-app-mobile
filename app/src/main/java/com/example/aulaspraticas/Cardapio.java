package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.adapter.CardapioAdapter;
import com.example.aulaspraticas.model.CardapioItem;

import java.util.ArrayList;
import java.util.List;


public class Cardapio extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardapioAdapter adapter;
    private List<CardapioItem> listaProdutos;


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


        listaProdutos = new ArrayList<>();
        listaProdutos.add(new CardapioItem(R.drawable.card_risoto, "Risoto de Cogumelos", "Arroz arbóreo com cogumelos frescos", "45.90", "⏱ 25 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_paste, "Penne ao Tomate", "Macarrão penne, tomate cereja e parmesão", "31.90", "⏱ 18 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_burguer, "Burguer da Casa", "Brioche, blend Angus e cheddar", "34.00", "⏱ 15 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_salad, "Salada Tropical", "Folhas verdes, frango grelhado e molho cítrico", "22.00", "⏱ 10 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_entrada, "Tábua Mediterrânea", "Homus, coalhada, legumes e pão sírio", "39.90", "⏱ 10 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_dessert, "Brownie Artesanal", "Chocolate belga, nozes e sorvete", "26.00", "⏱ 12 min", true));
        listaProdutos.add(new CardapioItem(R.drawable.card_juice, "Suco Refrescante", "Abacaxi, hortelã e limão", "15.00", "⏱ 5 min", true));

        adapter = new CardapioAdapter(listaProdutos);
        recyclerView.setAdapter(adapter);

    }
}

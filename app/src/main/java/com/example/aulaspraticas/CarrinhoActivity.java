package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.adapter.CarrinhoAdapter;
import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.model.CarrinhoItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoSingleton.OnCartChangeListener, CarrinhoAdapter.OnItemChangeListener {

    private RecyclerView recyclerViewCartItems;
    private TextView text_subtotal;
    private CarrinhoAdapter carrinhoAdapter;
    private CarrinhoSingleton carrinhoSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        recyclerViewCartItems = findViewById(R.id.recycler_carrinho);
        text_subtotal = findViewById(R.id.text_subtotal);

        carrinhoSingleton = CarrinhoSingleton.getInstance(this);
        carrinhoSingleton.registerListener(this);

        List<CarrinhoItem> itens = carrinhoSingleton.getItens();
        carrinhoAdapter = new CarrinhoAdapter(itens, this, this);
        recyclerViewCartItems.setAdapter(carrinhoAdapter);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        atualizarSubtotal();

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);
        Button botaoIrPagamento = findViewById(R.id.button_continuar_pagamento);

        botaoIrInicio.setOnClickListener(v -> startActivity(new Intent(CarrinhoActivity.this, HomePageActivity.class)));
        botaoIrCardapio.setOnClickListener(v -> startActivity(new Intent(CarrinhoActivity.this, CardapioActivity.class)));
        botaoIrPerfil.setOnClickListener(v -> startActivity(new Intent(CarrinhoActivity.this, PerfilActivity.class)));
        botaoIrPagamento.setOnClickListener(v -> {
            Intent intent = new Intent(CarrinhoActivity.this, EnderecosActivity.class);
            intent.putExtra("isCheckoutFlow", true);
            startActivity(intent);
        });
    }

    private void atualizarSubtotal() {
        double total = carrinhoSingleton.calcularTotal();
        NumberFormat formatBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        text_subtotal.setText("Subtotal: " + formatBR.format(total));
    }

    @Override
    public void onCartChanged() {
        runOnUiThread(() -> {
            List<CarrinhoItem> itens = carrinhoSingleton.getItens();
            carrinhoAdapter.atualizarItens(itens);
            atualizarSubtotal();
        });
    }

    @Override
    public void onItemQuantityChanged() {
        onCartChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carrinhoSingleton.unregisterListener(this);
    }
}

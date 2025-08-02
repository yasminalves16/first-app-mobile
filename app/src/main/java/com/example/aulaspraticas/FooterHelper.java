package com.example.aulaspraticas;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.model.CarrinhoItem;

import java.util.List;

public class FooterHelper {
    public static void setupFooter(AppCompatActivity activity) {
        CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(activity);
        TextView badge = activity.findViewById(R.id.carrinho_badge);
        atualizarBadge(badge, carrinho.getItens());

        Button botaoInicio = activity.findViewById(R.id.botao_inicio);
        Button botaoCardapio = activity.findViewById(R.id.botao_cardapio);
        Button botaoCarrinho = activity.findViewById(R.id.botao_carrinho);
        Button botaoPerfil = activity.findViewById(R.id.botao_perfil);

        botaoInicio.setOnClickListener(v -> activity.startActivity(new Intent(activity, HomePageActivity.class)));
        botaoCardapio.setOnClickListener(v -> activity.startActivity(new Intent(activity, CardapioActivity.class)));
        botaoCarrinho.setOnClickListener(v -> activity.startActivity(new Intent(activity, CarrinhoActivity.class)));
        botaoPerfil.setOnClickListener(v -> activity.startActivity(new Intent(activity, PerfilActivity.class)));

        carrinho.registerListener(() -> {
            activity.runOnUiThread(() -> atualizarBadge(badge, carrinho.getItens()));
        });
    }

    private static void atualizarBadge(TextView badge, List<CarrinhoItem> itens) {
        int quantidade = 0;
        for (CarrinhoItem item : itens) {
            quantidade += item.getQuantidade();
        }

        if (quantidade > 0) {
            badge.setVisibility(View.VISIBLE);
            badge.setText(String.valueOf(quantidade));
        } else {
            badge.setVisibility(View.GONE);
        }
    }
}

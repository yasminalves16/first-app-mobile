package com.example.aulaspraticas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.R;
import com.example.aulaspraticas.model.CardapioItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.CardapioViewHolder> {
    private List<CardapioItem> produtos;

    public CardapioAdapter(List<CardapioItem> produtos) {
        this.produtos = produtos;
    }

    public class CardapioViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagemProduto;
        public TextView nomeProduto;
        public TextView descricaoProduto;
        public TextView precoProduto;
        public TextView tempoPreparo;
        public TextView statusProduto;

        public CardapioViewHolder(View itemView) {
            super(itemView);
            imagemProduto = itemView.findViewById(R.id.imagem_prato);
            nomeProduto = itemView.findViewById(R.id.titulo_prato);
            descricaoProduto = itemView.findViewById(R.id.descricao_prato);
            precoProduto = itemView.findViewById(R.id.preco_prato);
            tempoPreparo = itemView.findViewById(R.id.tempo_preparo);
            statusProduto = itemView.findViewById(R.id.status_disponivel);
        }
    }

    @NonNull
    @Override
    public CardapioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_produtos, parent, false);
        return new CardapioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardapioViewHolder holder, int position) {
        CardapioItem item = produtos.get(position);

        holder.imagemProduto.setImageResource(item.imagem);
        holder.nomeProduto.setText(item.titulo);
        holder.descricaoProduto.setText(item.descricao);

        NumberFormat formatBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.precoProduto.setText(formatBR.format(Double.parseDouble(item.preco.replace("R$", "").replace(",", "."))));

        holder.tempoPreparo.setText(item.tempo);
        holder.statusProduto.setText(item.disponivel ? "Disponível" : "Indisponível");
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}

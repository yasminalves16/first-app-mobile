package com.example.aulaspraticas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aulaspraticas.ItemDetalheActivity;
import com.example.aulaspraticas.R;
import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.model.CardapioItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.CardapioViewHolder> {

    private final List<CardapioItem> produtos;
    private final Context context;

    public CardapioAdapter(Context context, List<CardapioItem> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    public static class CardapioViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagemProduto;
        public TextView nomeProduto;
        public TextView descricaoProduto;
        public TextView precoProduto;
        public TextView tempoPreparo;
        public TextView statusProduto;
        public TextView verMais;
        public Button botaoAdd;

        public CardapioViewHolder(View itemView) {
            super(itemView);
            imagemProduto = itemView.findViewById(R.id.imagem_prato);
            nomeProduto = itemView.findViewById(R.id.titulo_prato);
            descricaoProduto = itemView.findViewById(R.id.descricao_prato);
            precoProduto = itemView.findViewById(R.id.preco_prato);
            tempoPreparo = itemView.findViewById(R.id.tempo_preparo);
            statusProduto = itemView.findViewById(R.id.status_disponivel);
            verMais = itemView.findViewById(R.id.texto_ver_mais);
            botaoAdd = itemView.findViewById(R.id.botao_add);
        }
    }

    @NonNull
    @Override
    public CardapioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardapio, parent, false);
        return new CardapioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardapioViewHolder holder, int position) {
        CardapioItem item = produtos.get(position);


        Glide.with(holder.itemView.getContext())
                .load(item.getImagem())
                .placeholder(R.drawable.card_entrada)
                .into(holder.imagemProduto);

        holder.nomeProduto.setText(item.getTitulo());
        holder.descricaoProduto.setText(item.getDescricao());
        NumberFormat formatBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        try {
            double preco = Double.parseDouble(item.getPreco().replace(",", "."));
            holder.precoProduto.setText(formatBR.format(preco));
        } catch (NumberFormatException e) {
            holder.precoProduto.setText("R$ --");
        }
        holder.tempoPreparo.setText(item.getTempo());
        holder.verMais.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetalheActivity.class);
            intent.putExtra("produto", produtos.get(holder.getAdapterPosition()));
            context.startActivity(intent);
        });


        holder.statusProduto.setText(item.isDisponivel() ? "Disponível" : "Indisponível");

        holder.botaoAdd.setOnClickListener(v -> {
            CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(context);
            carrinho.adicionarProduto(item);
            Toast.makeText(context, item.getTitulo() + " adicionado ao carrinho", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}

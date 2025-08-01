package com.example.aulaspraticas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.R;
import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.model.CarrinhoItem;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>{

    private List<CarrinhoItem> itensCarrinho;
    private Context context;
    private OnItemChangeListener itemChangeListener;

    public interface OnItemChangeListener {
        void onItemQuantityChanged();
    }

    public CarrinhoAdapter(List<CarrinhoItem> itensCarrinho, Context context, OnItemChangeListener listener) {
        this.itensCarrinho = itensCarrinho;
        this.context = context;
        this.itemChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrinhoItem item = itensCarrinho.get(position);

        holder.textViewProductName.setText(item.getProduto().getTitulo());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantidade()));

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        double preco = 0.0;
        preco = Double.parseDouble(item.getProduto().getPreco());

        double subtotal = item.getSubtotal();
        holder.textViewProductSubtotal.setText(precoBr.format(subtotal));


        holder.textViewProductPrice.setText(precoBr.format(preco));
        holder.textViewProductSubtotal.setText(precoBr.format(subtotal));

        Glide.with(context)
                .load(item.getProduto().getImagem())
                .placeholder(R.drawable.card_entrada)
                .error(R.drawable.card_entrada)
                .into(holder.imageViewProduct);

        holder.buttonIncreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantidade() < 10) {
                CarrinhoSingleton.getInstance(context).adicionarProduto(item.getProduto());

                itemChangeListener.onItemQuantityChanged();
            } else {
                Toast.makeText(context, "Quantidade máxima atingida para " + item.getProduto().getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDecreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantidade() > 1) {
                CarrinhoSingleton.getInstance(context).removerQuantidade(item.getProduto(), 1);
                itemChangeListener.onItemQuantityChanged();
            } else {
                Toast.makeText(context, "Quantidade mínima atingida para " + item.getProduto().getTitulo() + ". Use o botão de remover para excluí-lo.", Toast.LENGTH_LONG).show();
            }
        });

        holder.buttonRemoveItem.setOnClickListener(v -> {
            CarrinhoSingleton.getInstance(context).removerProduto(item.getProduto());
            itemChangeListener.onItemQuantityChanged();
        });
    }

    @Override
    public int getItemCount() {
        return itensCarrinho.size();
    }

    public void atualizarItens(List<CarrinhoItem> novosItens) {
        this.itensCarrinho = novosItens;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewQuantity;
        TextView textViewProductSubtotal;
        Button buttonDecreaseQuantity;
        Button buttonIncreaseQuantity;
        ImageButton buttonRemoveItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.image_view_cart_product);
            textViewProductName = itemView.findViewById(R.id.text_view_cart_product_name);
            textViewProductPrice = itemView.findViewById(R.id.text_view_cart_product_price);
            textViewQuantity = itemView.findViewById(R.id.text_view_cart_quantity);
            textViewProductSubtotal = itemView.findViewById(R.id.text_view_cart_subtotal);
            buttonDecreaseQuantity = itemView.findViewById(R.id.button_decrease_cart_quantity);
            buttonIncreaseQuantity = itemView.findViewById(R.id.button_increase_cart_quantity);
            buttonRemoveItem = itemView.findViewById(R.id.button_remove_item);
        }
    }
}

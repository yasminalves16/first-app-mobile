package com.example.aulaspraticas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.R;
import com.example.aulaspraticas.model.CarrinhoItem;
import com.example.aulaspraticas.model.Pedido;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

    private List<Pedido> pedidos = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));

    public PedidosAdapter() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

    public void setPedidos(List<Pedido> novosPedidos) {
        this.pedidos = novosPedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.textViewOrderId.setText("Pedido #" + pedido.getId());
        holder.textViewOrderDate.setText("Data: " + dateFormat.format(pedido.getDataDoPedido()));
        holder.textViewOrderTotal.setText("Total: " + currencyFormat.format(pedido.getTotal()));
        holder.textViewOrderItems.setText("Itens: " + formatItems(pedido.getItens()));
        holder.textViewPayments.setText("Forma de pagamento: " + pedido.getMetodoPagamento());

    }

    private String formatItems(List<CarrinhoItem> itens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            CarrinhoItem item = itens.get(i);
            sb.append(item.getQuantidade()).append("x ").append(item.getProduto().getTitulo());
            if (i < itens.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewOrderDate, textViewOrderItems, textViewOrderTotal, textViewOrderStatus, textViewPayments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.text_view_order_id);
            textViewOrderDate = itemView.findViewById(R.id.text_view_order_date);
            textViewOrderItems = itemView.findViewById(R.id.text_view_order_items);
            textViewOrderTotal = itemView.findViewById(R.id.text_view_order_total);
            textViewPayments = itemView.findViewById(R.id.text_view_payment);
        }
    }
}

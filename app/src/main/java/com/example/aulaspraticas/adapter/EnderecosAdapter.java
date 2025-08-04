package com.example.aulaspraticas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.R;
import com.example.aulaspraticas.model.Endereco;

import java.util.List;

public class EnderecosAdapter extends RecyclerView.Adapter<EnderecosAdapter.ViewHolder> {

    public interface OnEnderecoClickListener {
        void onSelecionar(Endereco endereco);

        void onEditar(Endereco endereco, int position);

        void onRemover(Endereco endereco, int position);
    }

    private List<Endereco> enderecos;
    private OnEnderecoClickListener listener;
    private boolean isCheckoutFlow;

    public EnderecosAdapter(List<Endereco> enderecos, boolean isCheckoutFlow, OnEnderecoClickListener listener) {
        this.enderecos = enderecos;
        this.listener = listener;
        this.isCheckoutFlow = isCheckoutFlow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Endereco endereco = enderecos.get(position);
        holder.textViewEndereco.setText(formatarEndereco(endereco));

        if (isCheckoutFlow) {
            holder.btnSelecionar.setVisibility(View.VISIBLE);
            holder.btnSelecionar.setOnClickListener(v -> listener.onSelecionar(endereco));
        } else {
            holder.btnSelecionar.setVisibility(View.GONE);
        }

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(endereco, position));
        holder.btnRemover.setOnClickListener(v -> listener.onRemover(endereco, position));
    }

    @Override
    public int getItemCount() {
        return enderecos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEndereco;
        Button btnSelecionar, btnEditar, btnRemover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEndereco = itemView.findViewById(R.id.text_view_endereco);
            btnSelecionar = itemView.findViewById(R.id.btn_selecionar_endereco);
            btnEditar = itemView.findViewById(R.id.btn_editar_endereco);
            btnRemover = itemView.findViewById(R.id.btn_remover_endereco);
        }
    }

    private String formatarEndereco(Endereco endereco) {
        StringBuilder sb = new StringBuilder();

        sb.append(safe(endereco.getRua())).append(", ").append(safe(endereco.getNumero()));

        if (!isEmpty(endereco.getComplemento())) {
            sb.append(" - ").append(endereco.getComplemento());
        }

        if (!isEmpty(endereco.getBairro())) {
            sb.append(", ").append(endereco.getBairro());
        }

        if (!isEmpty(endereco.getCidade()) || !isEmpty(endereco.getEstado())) {
            sb.append(", ");
            if (!isEmpty(endereco.getCidade())) {
                sb.append(endereco.getCidade());
            }
            if (!isEmpty(endereco.getEstado())) {
                sb.append(" - ").append(endereco.getEstado());
            }
        }

        if (!isEmpty(endereco.getCep())) {
            sb.append(", ").append(endereco.getCep());
        }

        return sb.toString();
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}

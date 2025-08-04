package com.example.aulaspraticas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.CarrinhoItem;
import com.example.aulaspraticas.model.Endereco;
import com.example.aulaspraticas.model.Pedido;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.utils.Constants;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class PagamentoActivity extends AppCompatActivity {

    private TextView textViewSubtotalSummaryValue;
    private TextView textViewDeliveryFeeValue;
    private TextView textViewTotalValue;
    private Button buttonFinalizeOrder;
    private UsuarioApiClient usuarioApiClient;
    private double pedidoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        usuarioApiClient = UsuarioApiClient.getInstance(this);

        Button botaoFinalizar = findViewById(R.id.button_finalizar_pedido);
        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        textViewSubtotalSummaryValue = findViewById(R.id.text_view_subtotal_summary_value);
        textViewDeliveryFeeValue = findViewById(R.id.text_view_delivery_fee_value);
        textViewTotalValue = findViewById(R.id.text_view_total_value);
        buttonFinalizeOrder = findViewById(R.id.button_finalizar_pedido);

        Endereco enderecoSelecionado = (Endereco) getIntent().getSerializableExtra("enderecoSelecionado");
        TextView textViewEnderecoSelecionado = findViewById(R.id.text_view_endereco_selecionado);


        botaoIrInicio.setOnClickListener(view -> {
            Intent intent = new Intent(PagamentoActivity.this, HomePageActivity.class);
            startActivity(intent);
        });

        botaoIrCardapio.setOnClickListener(view -> {
            Intent intent = new Intent(PagamentoActivity.this, CardapioActivity.class);
            startActivity(intent);
        });

        botaoIrCarrinho.setOnClickListener(view -> {
            Intent intent = new Intent(PagamentoActivity.this, CarrinhoActivity.class);
            startActivity(intent);
        });

        botaoIrPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(PagamentoActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        FooterHelper.setupFooter(this);

        if (enderecoSelecionado != null) {
            String complemento = enderecoSelecionado.getComplemento();
            String enderecoFormatado = enderecoSelecionado.getRua() + ", " + enderecoSelecionado.getNumero() + ((complemento != null && !complemento.isEmpty()) ? " - " + complemento : "") + " - " + enderecoSelecionado.getBairro() + ", " + enderecoSelecionado.getCidade() + " - " + enderecoSelecionado.getEstado() + ", CEP: " + enderecoSelecionado.getCep();

            textViewEnderecoSelecionado.setText("Endereço: " + enderecoFormatado);
        } else {
            textViewEnderecoSelecionado.setText("Endereço não selecionado");
        }

        updateOrderSummary();

        buttonFinalizeOrder.setOnClickListener(v -> {
            fazerPagamento();
        });
    }

    private void updateOrderSummary() {
        CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(this);
        double subtotal = carrinho.calcularTotal();
        double deliveryFee = 10.00;
        this.pedidoTotal = subtotal + deliveryFee;

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        textViewSubtotalSummaryValue.setText(currencyFormat.format(subtotal));
        textViewDeliveryFeeValue.setText(currencyFormat.format(deliveryFee));
        textViewTotalValue.setText(currencyFormat.format(pedidoTotal));
    }

    private void fazerPagamento() {
        Usuario usuarioLogado = usuarioApiClient.getUsuarioLogado();

        if (usuarioLogado == null) {
            SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE);
            String idUsuario = preferences.getString("idusuario", null);

            if (idUsuario != null) {
                usuarioApiClient.obterUsuarioCompletoPorId(idUsuario, new UsuarioApiClient.UsuarioCompletoCallback() {
                    @Override
                    public void onSuccess(Usuario usuarioCompleto) {
                        usuarioApiClient.setUsuarioLogado(usuarioCompleto);
                        salvarPedido();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("PagamentoActivity", "Erro ao obter dados do usuário: " + errorMessage);
                        Toast.makeText(PagamentoActivity.this, "Erro ao carregar seus dados. Tente novamente.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Erro: Nenhum usuário logado. Faça o login novamente.", Toast.LENGTH_LONG).show();
            }
        } else {
            salvarPedido();
        }
    }

    private void salvarPedido() {
        CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(this);
        List<CarrinhoItem> itensCarrinho = carrinho.getItens();
        double total = carrinho.calcularTotal() + 10.00;
        String metodoPagamento = "Cartão de Crédito";

        Pedido novoPedido = new Pedido(itensCarrinho, total, metodoPagamento);

        usuarioApiClient.salvarNovoPedido(novoPedido, new UsuarioApiClient.SalvarPedidoCallback() {
            @Override
            public void onSuccess(Usuario usuarioComPedidoSalvo) {
                Log.d("PagamentoActivity", "Pedido salvo com sucesso! ID do usuário: " + usuarioComPedidoSalvo.getId());
                Toast.makeText(PagamentoActivity.this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();

                carrinho.limparCarrinho();

                Intent intent = new Intent(PagamentoActivity.this, PedidosActivity.class);
                intent.putExtra("pedido", novoPedido);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("PagamentoActivity", "Erro ao salvar o pedido: " + errorMessage);
                Toast.makeText(PagamentoActivity.this, "Erro ao salvar o pedido: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}

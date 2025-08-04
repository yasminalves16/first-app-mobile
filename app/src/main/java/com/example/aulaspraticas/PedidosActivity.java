package com.example.aulaspraticas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.adapter.PedidosAdapter;
import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.Pedido;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMyOrders;
    private PedidosAdapter pedidosAdapter;
    private UsuarioApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedidos);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidosActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidosActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidosActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PedidosActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        FooterHelper.setupFooter(this);

        apiClient = UsuarioApiClient.getInstance(this);

        recyclerViewMyOrders = findViewById(R.id.recycler_view_my_orders);
        recyclerViewMyOrders.setLayoutManager(new LinearLayoutManager(this));

        pedidosAdapter = new PedidosAdapter();
        recyclerViewMyOrders.setAdapter(pedidosAdapter);

        loadUserOrders();

    }

    private void loadUserOrders() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, Context.MODE_PRIVATE);
        String userId = sharedPref.getString("idusuario", null);

        if (userId != null) {
            apiClient.obterUsuarioCompletoPorId(userId, new UsuarioApiClient.UsuarioCompletoCallback() {
                @Override
                public void onSuccess(Usuario usuarioCompleto) {
                    Log.d("MyOrdersActivity", "Pedidos carregados com sucesso para o usuário: " + usuarioCompleto.getName());

                    if (usuarioCompleto.getPedidos() != null && !usuarioCompleto.getPedidos().isEmpty()) {
                        List<Pedido> pedidos = new ArrayList<>();
                        for (Object pedidoObj : usuarioCompleto.getPedidos()) {
                            if (pedidoObj instanceof Pedido) {
                                pedidos.add((Pedido) pedidoObj);
                            }
                        }

                        Collections.sort(pedidos, new Comparator<Pedido>() {
                            @Override
                            public int compare(Pedido p1, Pedido p2) {
                                return p2.getDataDoPedido().compareTo(p1.getDataDoPedido());
                            }
                        });
                        pedidosAdapter.setPedidos(pedidos);
                    } else {
                        Log.d("MyOrdersActivity", "Nenhum pedido encontrado para o usuário.");
                        pedidosAdapter.setPedidos(new ArrayList<>());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("MyOrdersActivity", "Erro ao carregar pedidos: " + errorMessage);
                }
            });
        } else {
            Log.e("MyOrdersActivity", "Nenhum ID de usuário salvo. Redirecionando para login.");
        }
    }
}
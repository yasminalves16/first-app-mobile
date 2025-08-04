package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aulaspraticas.adapter.EnderecosAdapter;
import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.Endereco;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.utils.Constants;

import java.util.List;

public class EnderecosActivity extends AppCompatActivity {
    private RecyclerView recyclerViewEnderecos;
    private EnderecosAdapter adapter;
    private UsuarioApiClient apiClient;
    private Usuario usuarioLogado;
    private boolean isCheckoutFlow;

    private ActivityResultLauncher<Intent> enderecoFormLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);

        isCheckoutFlow = getIntent().getBooleanExtra("isCheckoutFlow", false);

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);
        Button botaoAdicionarEndereco = findViewById(R.id.botao_adicionar_endereco);

        recyclerViewEnderecos = findViewById(R.id.recycler_view_enderecos);
        recyclerViewEnderecos.setLayoutManager(new LinearLayoutManager(this));

        apiClient = UsuarioApiClient.getInstance(this);

        enderecoFormLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                Endereco endereco = (Endereco) data.getSerializableExtra("endereco");
                int position = data.getIntExtra("position", -1);

                if (position == -1) {
                    usuarioLogado.getEnderecos().add(endereco);
                    adapter.notifyItemInserted(usuarioLogado.getEnderecos().size() - 1);
                } else {
                    usuarioLogado.getEnderecos().set(position, endereco);
                    adapter.notifyItemChanged(position);
                }
                atualizarUsuarioNoServidor();
            }
        });

        carregarUsuario();

        botaoIrInicio.setOnClickListener(view -> startActivity(new Intent(this, HomePageActivity.class)));
        botaoIrCardapio.setOnClickListener(view -> startActivity(new Intent(this, CardapioActivity.class)));
        botaoIrCarrinho.setOnClickListener(view -> startActivity(new Intent(this, CarrinhoActivity.class)));
        botaoIrPerfil.setOnClickListener(view -> startActivity(new Intent(this, PerfilActivity.class)));

        botaoAdicionarEndereco.setOnClickListener(v -> {
            Intent intent = new Intent(this, EnderecoFormularioActivity.class);
            enderecoFormLauncher.launch(intent);
        });

        FooterHelper.setupFooter(this);
    }

    private void carregarUsuario() {
        String userId = getSharedPreferences(Constants.SHARED_PREFERENCES_AUTENTICACAO, MODE_PRIVATE).getString("idusuario", null);

        if (userId != null) {
            apiClient.obterUsuarioCompletoPorId(userId, new UsuarioApiClient.UsuarioCompletoCallback() {
                @Override
                public void onSuccess(Usuario usuario) {
                    usuarioLogado = usuario;
                    runOnUiThread(() -> configurarAdapter(usuarioLogado.getEnderecos()));
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> Toast.makeText(EnderecosActivity.this, "Erro ao carregar endereços", Toast.LENGTH_LONG).show());
                }
            });
        }
    }

    private void configurarAdapter(List<Endereco> enderecos) {
        adapter = new EnderecosAdapter(enderecos, isCheckoutFlow, new EnderecosAdapter.OnEnderecoClickListener() {
            @Override
            public void onSelecionar(Endereco endereco) {
                if (isCheckoutFlow) {
                    Intent intent = new Intent(EnderecosActivity.this, PagamentoActivity.class);
                    intent.putExtra("enderecoSelecionado", endereco);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onEditar(Endereco endereco, int position) {
                Intent intent = new Intent(EnderecosActivity.this, EnderecoFormularioActivity.class);
                intent.putExtra("endereco", endereco);
                intent.putExtra("position", position);
                enderecoFormLauncher.launch(intent);
            }

            @Override
            public void onRemover(Endereco endereco, int position) {
                usuarioLogado.getEnderecos().remove(position);
                atualizarUsuarioNoServidorRemocao(position);
            }
        });

        recyclerViewEnderecos.setAdapter(adapter);
    }

    private void atualizarUsuarioNoServidor() {
        apiClient.atualizarUsuarioCompleto(usuarioLogado, new UsuarioApiClient.AtualizarUsuarioCompletoCallback() {
            @Override
            public void onSuccess(Usuario usuarioAtualizado) {
                runOnUiThread(() -> Toast.makeText(EnderecosActivity.this, "Endereço salvo com sucesso", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(EnderecosActivity.this, "Erro ao salvar endereço", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void atualizarUsuarioNoServidorRemocao(int positionRemovido) {
        apiClient.atualizarUsuarioCompleto(usuarioLogado, new UsuarioApiClient.AtualizarUsuarioCompletoCallback() {
            @Override
            public void onSuccess(Usuario usuarioAtualizado) {
                runOnUiThread(() -> {
                    adapter.notifyItemRemoved(positionRemovido);
                    Toast.makeText(EnderecosActivity.this, "Endereço removido com sucesso", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(EnderecosActivity.this, "Erro ao remover endereço", Toast.LENGTH_SHORT).show());
            }
        });
    }
}

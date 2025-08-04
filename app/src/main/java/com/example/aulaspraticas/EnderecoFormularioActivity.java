package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.Endereco;
import com.example.aulaspraticas.model.Usuario;

public class EnderecoFormularioActivity extends AppCompatActivity {

    private EditText editRua, editNumero, editComplemento, editBairro, editCidade, editEstado, editCep;
    private Button botaoSalvar;

    private Usuario usuarioLogado;
    private Endereco enderecoEditando;
    private int positionEditando = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco_formulario);

        editRua = findViewById(R.id.edit_rua);
        editNumero = findViewById(R.id.edit_numero);
        editComplemento = findViewById(R.id.edit_complemento);
        editBairro = findViewById(R.id.edit_bairro);
        editCidade = findViewById(R.id.edit_cidade);
        editEstado = findViewById(R.id.edit_estado);
        editCep = findViewById(R.id.edit_cep);
        botaoSalvar = findViewById(R.id.botao_salvar_endereco);

        usuarioLogado = UsuarioApiClient.getInstance(this).getUsuarioLogado();

        Intent intent = getIntent();
        if (intent.hasExtra("endereco")) {
            enderecoEditando = (Endereco) intent.getSerializableExtra("endereco");
            positionEditando = intent.getIntExtra("position", -1);
            preencherCampos(enderecoEditando);
        }

        botaoSalvar.setOnClickListener(v -> salvarEndereco());

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        botaoIrInicio.setOnClickListener(view -> startActivity(new Intent(this, HomePageActivity.class)));
        botaoIrCardapio.setOnClickListener(view -> startActivity(new Intent(this, CardapioActivity.class)));
        botaoIrCarrinho.setOnClickListener(view -> startActivity(new Intent(this, CarrinhoActivity.class)));
        botaoIrPerfil.setOnClickListener(view -> startActivity(new Intent(this, PerfilActivity.class)));

        FooterHelper.setupFooter(this);
    }

    private void preencherCampos(Endereco endereco) {
        editRua.setText(endereco.getRua());
        editNumero.setText(endereco.getNumero());
        editComplemento.setText(endereco.getComplemento());
        editBairro.setText(endereco.getBairro());
        editCidade.setText(endereco.getCidade());
        editEstado.setText(endereco.getEstado());
        editCep.setText(endereco.getCep());
    }

    private void salvarEndereco() {
        String rua = editRua.getText().toString().trim();
        String numero = editNumero.getText().toString().trim();
        String complemento = editComplemento.getText().toString().trim();
        String bairro = editBairro.getText().toString().trim();
        String cidade = editCidade.getText().toString().trim();
        String estado = editEstado.getText().toString().trim();
        String cep = editCep.getText().toString().trim();

        if (rua.isEmpty() || numero.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || estado.isEmpty() || cep.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        Endereco novoEndereco = new Endereco(rua, numero, complemento, bairro, cidade, estado, cep);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("endereco", novoEndereco);
        resultIntent.putExtra("position", positionEditando);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

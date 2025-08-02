package com.example.aulaspraticas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextTelefone;
    private EditText editTextSenhaAtual, editTextNovaSenha, editTextConfirmarNovaSenha;
    private Button btnSalvarAlteracoes;

    private UsuarioApiClient usuarioApiClient;
    private SharedPreferences preferences;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextSenhaAtual = findViewById(R.id.editTextSenhaAtual);
        editTextNovaSenha = findViewById(R.id.editTextNovaSenha);
        editTextConfirmarNovaSenha = findViewById(R.id.editTextConfirmarNovaSenha);
        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAlteracoes);

        usuarioApiClient = UsuarioApiClient.getInstance(this);
        preferences = getSharedPreferences("Restaurante.autenticacao", MODE_PRIVATE);
        idUsuario = preferences.getString("idusuario", null);

        editTextNome.setText(preferences.getString("nomeUsuario", ""));
        editTextEmail.setText(preferences.getString("emailUsuario", ""));
        editTextTelefone.setText(preferences.getString("telefoneUsuario", ""));

        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        btnSalvarAlteracoes.setOnClickListener(v -> {
            String nomeDigitado = editTextNome.getText().toString().trim();
            String emailDigitado = editTextEmail.getText().toString().trim();
            String telefoneDigitado = editTextTelefone.getText().toString().trim();

            String senhaAtual = editTextSenhaAtual.getText().toString().trim();
            String novaSenha = editTextNovaSenha.getText().toString().trim();
            String confirmarNovaSenha = editTextConfirmarNovaSenha.getText().toString().trim();

            if (!senhaAtual.isEmpty() || !novaSenha.isEmpty() || !confirmarNovaSenha.isEmpty()) {
                if (senhaAtual.isEmpty()) {
                    editTextSenhaAtual.setError("Informe sua senha atual");
                    return;
                }

                if (novaSenha.length() < 4) {
                    editTextNovaSenha.setError("Nova senha deve ter pelo menos 4 caracteres");
                    return;
                }

                if (!novaSenha.equals(confirmarNovaSenha)) {
                    editTextConfirmarNovaSenha.setError("As senhas não coincidem");
                    return;
                }

                String emailParaVerificacao = preferences.getString("emailUsuario", "");
                usuarioApiClient.autenticarUsuario(emailParaVerificacao, senhaAtual, new UsuarioApiClient.LoginCallback() {
                    @Override
                    public void onSuccess(Usuario usuarioLogado) {
                        atualizarDados(nomeDigitado, emailDigitado, telefoneDigitado, novaSenha);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(EditarPerfilActivity.this, "Erro de verificação: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCredenciaisInvalidas() {
                        Toast.makeText(EditarPerfilActivity.this, "Senha atual incorreta.", Toast.LENGTH_SHORT).show();
                        editTextSenhaAtual.setError("Senha incorreta");
                    }
                });

            } else {
                atualizarDados(nomeDigitado, emailDigitado, telefoneDigitado, "");
            }
        });

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EditarPerfilActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EditarPerfilActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EditarPerfilActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(EditarPerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        FooterHelper.setupFooter(this);
    }

    private void atualizarDados(String nome, String email, String telefone, String novaSenha) {
        String nomeFinal = nome.isEmpty() ? preferences.getString("nomeUsuario", "") : nome;
        String emailFinal = email.isEmpty() ? preferences.getString("emailUsuario", "") : email;
        String telefoneFinal = telefone.isEmpty() ? preferences.getString("telefoneUsuario", "") : telefone;

        Usuario usuarioAtualizado = new Usuario(nomeFinal, emailFinal, telefoneFinal, novaSenha);
        usuarioAtualizado.setId(idUsuario);

        usuarioApiClient.editarUsuario(usuarioAtualizado, new UsuarioApiClient.UpdateUserCallback() {
            @Override
            public void onSuccess(Usuario usuario) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nomeUsuario", nomeFinal);
                editor.putString("emailUsuario", emailFinal);
                editor.putString("telefoneUsuario", telefoneFinal);
                editor.apply();

                Toast.makeText(EditarPerfilActivity.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditarPerfilActivity.this, PerfilActivity.class));
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EditarPerfilActivity.this, "Erro ao atualizar: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

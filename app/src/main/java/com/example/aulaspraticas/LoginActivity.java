package com.example.aulaspraticas;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aulaspraticas.data.remote.UsuarioApiClient;
import com.example.aulaspraticas.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private TextView textViewForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        editTextEmailAddress = findViewById(R.id.editEmailAddress);
        editTextPassword = findViewById(R.id.editPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                View errorFocusView = null;

                if (email.isEmpty()) {
                    editTextEmailAddress.setError("Campo de email é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextEmailAddress;
                    }
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    if (errorFocusView == null) {
                        editTextEmailAddress.setError("Informe um e-mail válido");
                        errorFocusView = editTextEmailAddress;
                    }
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Campo de senha é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPassword;
                    }
                }

                if (editTextPassword.length() < 4) {
                    editTextPassword.setError("A senha precisa conter no mínimo 4 caracteres");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPassword;
                    }
                }

                if (errorFocusView != null) {
                    errorFocusView.requestFocus();
                    Toast.makeText(LoginActivity.this, "Erro nos dados de entrada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Verificando credenciais...", Toast.LENGTH_SHORT).show();

                    UsuarioApiClient.getInstance(LoginActivity.this).autenticarUsuario(email, password, new UsuarioApiClient.LoginCallback() {
                        @Override
                        public void onSuccess(Usuario usuarioLogado) {
                            Toast.makeText(LoginActivity.this, "Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getName() + "!", Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getSharedPreferences("Restaurante.autenticacao", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLogged", true);
                            editor.putString("emailUsuario", usuarioLogado.getEmail());
                            editor.putString("nomeUsuario", usuarioLogado.getName());
                            editor.putString("telefoneUsuario", usuarioLogado.getPhone());
                            editor.putString("idusuario", usuarioLogado.getId());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(LoginActivity.this, "Erro de comunicação: " + errorMessage, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCredenciaisInvalidas() {
                            Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show();
                            editTextEmailAddress.setError("Verifique suas credenciais");
                            editTextPassword.setError("Verifique suas credenciais");
                            editTextEmailAddress.requestFocus();
                        }
                    });
                }


            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(intent);
            }
        });
    }
}
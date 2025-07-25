package com.example.aulaspraticas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.data.remote.UsuarioApiClient;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        Button buttonCadastrar = findViewById(R.id.buttonRegister);
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        EditText editTextName = findViewById(R.id.editName);
        EditText editTextPhone = findViewById(R.id.editPhoneNumber);
        EditText editTextEmail = findViewById(R.id.editEmailAddress);
        EditText editTextPassword = findViewById(R.id.editPassword);
        EditText editTextPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString().trim();
                View errorFocusView = null;

                if (name.isEmpty()) {
                    editTextName.setError("Campo nome é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextName;
                    }
                }

                if (phone.isEmpty()) {
                    editTextPhone.setError("Campo telefone é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPhone;
                    }
                } else if (phone.length() < 8) {
                    editTextPhone.setError("Numero de telefone incompleto");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPhone;
                    }
                }

                if (email.isEmpty()) {
                    editTextEmail.setError("Campo e-mail é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextEmail;
                    }
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Informe um e-mail válido");
                    if (errorFocusView == null) {
                        errorFocusView = editTextEmail;
                    }
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Campo senha é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPassword;
                    }
                } else if (password.length() < 4) {
                    editTextPassword.setError("A senha precisa conter no mínimo 4 caracteres");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPassword;
                    }
                }

                if (passwordConfirmation.isEmpty()) {
                    editTextPasswordConfirmation.setError("Campo confirmação de senha é obrigatório");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPasswordConfirmation;
                    }
                } else if (!passwordConfirmation.equals(password)) {
                    editTextPasswordConfirmation.setError("As senhas precisam corresponder");
                    if (errorFocusView == null) {
                        errorFocusView = editTextPasswordConfirmation;
                    }
                }

                if (errorFocusView != null) {
                    errorFocusView.requestFocus();
                    Toast.makeText(Cadastro.this, "Reveja os campos indicados com erro", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cadastro.this, "Verificando e-mail...", Toast.LENGTH_SHORT).show();

                    UsuarioApiClient.getInstance(Cadastro.this).verificarEmailUnico(email, new UsuarioApiClient.EmailCheckCallback() {
                        @Override
                        public void onSuccess(boolean isUnique) {
                            if (isUnique) {
                                Usuario novoUsuario = new Usuario(name, email, phone, password);

                                UsuarioApiClient.getInstance(Cadastro.this).cadastrarUsuario(novoUsuario, new UsuarioApiClient.RegisterUserCallback() {
                                    @Override
                                    public void onSuccess(Usuario usuarioCadastrado) {
                                        Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Cadastro.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        Toast.makeText(Cadastro.this, "Falha no cadastro: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                editTextEmail.setError("Este e-mail já está cadastrado.");
                                editTextEmail.requestFocus();
                                Toast.makeText(Cadastro.this, "E-mail já cadastrado.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(Cadastro.this, "Erro ao verificar e-mail: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
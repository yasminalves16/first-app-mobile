package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aulaspraticas.data.remote.UsuarioApiClient;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextPasswordConfirmation;
    private UsuarioApiClient usuarioApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_senha);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editPassword);
        editTextPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewRegister = findViewById(R.id.textViewLogin);

        usuarioApiClient = UsuarioApiClient.getInstance(this);

        buttonRegister.setOnClickListener(v -> {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String passwordConfirmation = editTextPasswordConfirmation.getText().toString().trim();
            View errorFocusView = null;

            if (email.isEmpty()) {
                editTextEmail.setError("Campo e-mail é obrigatório");
                errorFocusView = editTextEmail;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Campo senha é obrigatório");
                if (errorFocusView == null) errorFocusView = editTextPassword;
            } else if (password.length() < 4) {
                editTextPassword.setError("A senha precisa conter no mínimo 4 caracteres");
                if (errorFocusView == null) errorFocusView = editTextPassword;
            }

            if (passwordConfirmation.isEmpty()) {
                editTextPasswordConfirmation.setError("Campo confirmação de senha é obrigatório");
                if (errorFocusView == null) errorFocusView = editTextPasswordConfirmation;
            } else if (!passwordConfirmation.equals(password)) {
                editTextPasswordConfirmation.setError("As senhas precisam corresponder");
                if (errorFocusView == null) errorFocusView = editTextPasswordConfirmation;
            }

            if (errorFocusView != null) {
                errorFocusView.requestFocus();
                Toast.makeText(this, "Reveja os campos indicados com erro", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioApiClient.recuperarSenha(email, password, new UsuarioApiClient.RecuperarSenhaCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(RecuperarSenhaActivity.this, "Caso tenha digitado um e-mail válido, faça login com seu email e senha", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RecuperarSenhaActivity.this, LoginActivity.class));
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(RecuperarSenhaActivity.this, "Caso tenha digitado um e-mail válido, faça login com seu email e senha", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RecuperarSenhaActivity.this, LoginActivity.class));
                    finish();
                }
            });
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(RecuperarSenhaActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }
}

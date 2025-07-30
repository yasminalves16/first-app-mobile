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

public class RecuperarSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_senha);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewRegister = findViewById(R.id.textViewLogin);
        EditText editTextPassword = findViewById(R.id.editPassword);
        EditText editTextPasswordConfirmation = findViewById(R.id.editPasswordConfirmation);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = editTextPassword.getText().toString().trim();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString().trim();
                View errorFocusView = null;

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
                    Toast.makeText(RecuperarSenhaActivity.this, "Reveja os campos indicados com erro", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RecuperarSenhaActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecuperarSenhaActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}
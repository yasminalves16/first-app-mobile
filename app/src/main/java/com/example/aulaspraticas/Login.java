package com.example.aulaspraticas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private TextView textViewForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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

                if(email.isEmpty()){
                    editTextEmailAddress.setError("Campo de email é obrigatório");
                    if(errorFocusView == null){
                        errorFocusView = editTextEmailAddress;
                    }
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    if(errorFocusView == null){
                        editTextEmailAddress.setError("Informe um e-mail válido");
                        errorFocusView = editTextEmailAddress;
                    }
                }

                if(password.isEmpty()){
                    editTextPassword.setError("Campo de senha é obrigatório");
                    if(errorFocusView == null){
                        errorFocusView = editTextPassword;
                    }
                }

                if (editTextPassword.length() < 4){
                    editTextPassword.setError("A senha precisa conter no mínimo 4 caracteres");
                    if(errorFocusView == null){
                        errorFocusView = editTextPassword;
                    }
                }

                if(errorFocusView != null){
                    errorFocusView.requestFocus();
                    Toast.makeText(Login.this, "Erro ...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Logado com sucesso...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, HomePage.class);
                    startActivity(intent);
                }

            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Login.this, RecuperarSenha.class);
                startActivity(intent);
            }
        });
    }
}
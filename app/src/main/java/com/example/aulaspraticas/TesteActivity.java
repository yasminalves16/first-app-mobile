package com.example.aulaspraticas;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aulaspraticas.model.Pessoa;

public class TesteActivity extends AppCompatActivity {

    TextView tvNomePessoa;
    TextView tvIdadePessoa;
    TextView tvPetPessoa;
    TextView tvCidadePessoa;
    TextView tvEmailPessoa;
    TextView tvTelefonePessoa;
    EditText etNomePessoa;
    EditText etIdadePessoa;
    EditText etPetPessoa;
    EditText etCidadePessoa;
    EditText etEmailPessoa;
    EditText etTelefonePessoa;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        tvNomePessoa = findViewById(R.id.tvNomePessoa);
        tvIdadePessoa = findViewById(R.id.tvIdadePessoa);
        tvPetPessoa = findViewById(R.id.tvPetPessoa);
        tvCidadePessoa = findViewById(R.id.tvCidadePessoa);
        tvEmailPessoa = findViewById(R.id.tvEmailPessoa);
        tvTelefonePessoa = findViewById(R.id.tvTelefonePessoa);
        etNomePessoa = findViewById(R.id.etNomePessoa);
        etIdadePessoa = findViewById(R.id.etIdadePessoa);
        etPetPessoa = findViewById(R.id.etPetPessoa);
        etCidadePessoa = findViewById(R.id.etCidadePessoa);
        etEmailPessoa = findViewById(R.id.etEmailPessoa);
        etTelefonePessoa = findViewById(R.id.etTelefonePessoa);

    }

    public void cadastrar(View view) {
        Pessoa pessoa = new Pessoa(
                etNomePessoa.getText().toString(),
                Integer.parseInt(etIdadePessoa.getText().toString()),
                etPetPessoa.getText().toString(),
                etCidadePessoa.getText().toString(),
                etEmailPessoa.getText().toString(),
                etTelefonePessoa.getText().toString()
        );

        tvNomePessoa.setText("Nome: " + pessoa.getNome());
        tvIdadePessoa.setText("Idade: " + pessoa.getIdade());
        tvPetPessoa.setText("Pet: " + pessoa.getPet());
        tvCidadePessoa.setText("Cidade: " + pessoa.getCidade());
        tvEmailPessoa.setText("Email: " + pessoa.getEmail());
        tvTelefonePessoa.setText("Telefone: " + pessoa.getTelefone());
    }
}
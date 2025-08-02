package com.example.aulaspraticas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.aulaspraticas.data.remote.CarrinhoSingleton;
import com.example.aulaspraticas.model.CardapioItem;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemDetalheActivity extends AppCompatActivity {

    private TextView titulo, descricao, preco, tempo, ingredientes, infoAdicional;
    private ImageView imagem;
    private Button botaoAdicionar;
    private int quantidadeSelecionada = 1;
    private TextView textQuantidade;
    private Button botaoAumentar, botaoDiminuir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detalhe);


        titulo = findViewById(R.id.titulo_detalhe);
        descricao = findViewById(R.id.descricao_detalhe);
        preco = findViewById(R.id.preco_detalhe);
        tempo = findViewById(R.id.tempo_detalhe);
        ingredientes = findViewById(R.id.ingredientes_texto);
        infoAdicional = findViewById(R.id.info_adicional);
        imagem = findViewById(R.id.imagem_detalhe);
        botaoAdicionar = findViewById(R.id.botao_adicionar_carrinho);
        textQuantidade = findViewById(R.id.text_quantity);
        botaoAumentar = findViewById(R.id.button_increase);
        botaoDiminuir = findViewById(R.id.button_decrease);

        CardapioItem produto = (CardapioItem) getIntent().getSerializableExtra("produto");
        Button botaoIrInicio = findViewById(R.id.botao_inicio);
        Button botaoIrCardapio = findViewById(R.id.botao_cardapio);
        Button botaoIrCarrinho = findViewById(R.id.botao_carrinho);
        Button botaoIrPerfil = findViewById(R.id.botao_perfil);

        if (produto != null) {
            titulo.setText(produto.getTitulo());
            descricao.setText(produto.getDescricao());

            try {
                double valor = Double.parseDouble(produto.getPreco().replace(",", "."));
                preco.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor));
            } catch (NumberFormatException e) {
                preco.setText("R$ --");
            }

            tempo.setText(produto.getTempo());
            ingredientes.setText(String.join(", ", produto.getIngredientes()));

            String adicionais = "";
            if (!produto.getAlergenicos().isEmpty()) {
                adicionais += "Alergênicos: " + String.join(", ", produto.getAlergenicos()) + ". ";
            }
            if (produto.isGluten()) {
                adicionais += "Contém glúten.";
            } else {
                adicionais += "Sem glúten.";
            }
            infoAdicional.setText(adicionais);

            // Carrega imagem
            Glide.with(this)
                    .load(produto.getImagem())
                    .placeholder(R.drawable.card_entrada)
                    .into(imagem);
        }

        botaoIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ItemDetalheActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ItemDetalheActivity.this, CardapioActivity.class);
                startActivity(intent);
            }
        });

        botaoIrCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ItemDetalheActivity.this, CarrinhoActivity.class);
                startActivity(intent);
            }
        });

        botaoIrPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ItemDetalheActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        botaoAumentar.setOnClickListener(v -> {
            quantidadeSelecionada++;
            textQuantidade.setText(String.valueOf(quantidadeSelecionada));
        });

        botaoDiminuir.setOnClickListener(v -> {
            if (quantidadeSelecionada > 1) {
                quantidadeSelecionada--;
                textQuantidade.setText(String.valueOf(quantidadeSelecionada));
            }
        });

        botaoAdicionar.setOnClickListener(v -> {
            if (produto != null) {
                CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance(ItemDetalheActivity.this);
                carrinho.adicionarProduto(produto, quantidadeSelecionada);
                FooterHelper.setupFooter(this);
                Toast.makeText(this, "“" + produto.getTitulo() + "” foi adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
            }
        });

        FooterHelper.setupFooter(this);
    }
}

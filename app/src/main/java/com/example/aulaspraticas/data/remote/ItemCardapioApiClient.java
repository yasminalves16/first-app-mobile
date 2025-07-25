package com.example.aulaspraticas.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aulaspraticas.model.CardapioItem;
import com.example.aulaspraticas.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemCardapioApiClient extends BaseApiClient {

    private static ItemCardapioApiClient instance;


    private ItemCardapioApiClient(Context ctx) {
        super(ctx);
    }

    public static synchronized ItemCardapioApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ItemCardapioApiClient(context);
        }
        return instance;
    }

    public interface ProdutosCallback {
        void onSuccess(List<CardapioItem> produtos);

        void onError(String errorMessage);
    }

    public void listagemProdutos(final ProdutosCallback callback) {
        String url = Constants.BASE_URL + "products";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<CardapioItem> produtos = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject produtoJson = response.getJSONObject(i);

                        int id = Integer.parseInt(produtoJson.getString("id"));
                        String nome = produtoJson.getString("nome");
                        String descricao = produtoJson.getString("descricao");
                        String preco = produtoJson.getString("preco");
                        String tempo = produtoJson.getString("tempo");
                        String imagem = produtoJson.getString("imagem");
                        boolean disponivel = produtoJson.getBoolean("disponivel");

                        CardapioItem item = new CardapioItem(id, imagem, nome, descricao, preco, tempo, disponivel);
                        produtos.add(item);

                    } catch (JSONException e) {
                        Log.e("Produto Api Client", e.getMessage(), e);
                        callback.onError(e.getMessage());
                    }
                }

                callback.onSuccess(produtos);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        String errorMessage = "Erro ao obter produtos.";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        callback.onError(errorMessage);
                    }
                }
        );

        addToRequestQueue(jsonArrayRequest);
    }
}


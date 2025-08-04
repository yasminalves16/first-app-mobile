package com.example.aulaspraticas.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aulaspraticas.adapter.CardapioAdapter;
import com.example.aulaspraticas.model.CardapioItem;
import com.example.aulaspraticas.model.CarrinhoItem;
import com.example.aulaspraticas.model.Endereco;
import com.example.aulaspraticas.model.Pedido;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class UsuarioApiClient {
    private static UsuarioApiClient instance;
    private Usuario usuarioLogado;
    private SimpleDateFormat dateFormat;
    private RequestQueue requestQueue;

    private void addToRequestQueue(JsonObjectRequest request) {
        requestQueue.add(request);
    }

    private void addToRequestQueue(JsonArrayRequest request) {
        requestQueue.add(request);
    }

    private UsuarioApiClient(Context context) {
        super();
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

    public static synchronized UsuarioApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioApiClient(context);
        }
        return instance;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public interface EmailCheckCallback {
        void onSuccess(boolean isUnique);

        void onError(String errorMessage);
    }

    public interface RegisterUserCallback {
        void onSuccess(Usuario registeredUser);

        void onError(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(Usuario usuarioLogado);

        void onError(String errorMessage);

        void onCredenciaisInvalidas();
    }

    public interface UpdateUserCallback {
        void onSuccess(Usuario usuarioAtualizado);

        void onError(String errorMessage);
    }

    public interface RecuperarSenhaCallback {
        void onSuccess();

        void onError(String errorMessage);
    }

    public interface UsuarioPorIdCallback {
        void onSuccess(Usuario usuario);

        void onError(String errorMessage);
    }

    public interface UsuarioCompletoCallback {
        void onSuccess(Usuario usuarioCompleto);

        void onError(String errorMessage);
    }

    public interface AtualizarUsuarioCompletoCallback {
        void onSuccess(Usuario usuarioAtualizado);

        void onError(String errorMessage);
    }

    public interface SalvarPedidoCallback {
        void onSuccess(Usuario usuarioComPedidoSalvo);

        void onError(String errorMessage);
    }

    public void verificarEmailUnico(String email, final EmailCheckCallback callback) {
        String url = Constants.BASE_URL + "users?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> callback.onSuccess(response.length() == 0), error -> {
            String errorMessage = "Erro ao verificar unicidade do e-mail.";
            if (error != null && error.getMessage() != null) {
                errorMessage += ": " + error.getMessage();
            }
            Log.e("UsuarioApiClient", errorMessage, error);
            callback.onError(errorMessage);
        });
        addToRequestQueue(jsonArrayRequest);
    }

    public void cadastrarUsuario(Usuario usuario, final RegisterUserCallback callback) {
        String url = Constants.BASE_URL + "users";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", usuario.getName());
            requestBody.put("email", usuario.getEmail());
            requestBody.put("phone", usuario.getPhone());
            requestBody.put("password", usuario.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError("Erro ao preparar dados do usuário para cadastro.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, response -> {
            try {
                String id = response.getString("id");
                usuario.setId(id);
                callback.onSuccess(usuario);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onError("Erro ao processar resposta de cadastro.");
            }
        }, error -> {
            String errorMessage = "Erro ao cadastrar usuário.";
            if (error != null && error.networkResponse != null) {
                errorMessage += " Status: " + error.networkResponse.statusCode;
            } else if (error != null && error.getMessage() != null) {
                errorMessage += ": " + error.getMessage();
            }
            Log.e("UsuarioApiClient", errorMessage, error);
            callback.onError(errorMessage);
        });
        addToRequestQueue(jsonObjectRequest);
    }

    public void autenticarUsuario(String email, String senha, final LoginCallback callback) {
        String url = Constants.BASE_URL + "users?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response.length() > 0) {
                try {
                    JSONObject usuarioJson = response.getJSONObject(0);
                    String id = usuarioJson.getString("id");
                    String name = usuarioJson.getString("name");
                    String emailApi = usuarioJson.getString("email");
                    String phone = usuarioJson.getString("phone");
                    String senhaApi = usuarioJson.getString("password");

                    if (senhaApi.equals(senha)) {
                        Usuario usuarioAutenticado = new Usuario(name, emailApi, phone, senhaApi);
                        usuarioAutenticado.setId(id);
                        callback.onSuccess(usuarioAutenticado);
                    } else {
                        callback.onCredenciaisInvalidas();
                    }
                } catch (JSONException e) {
                    Log.e("UsuarioApiClient", "Erro ao parsear JSON de usuário para login: " + e.getMessage(), e);
                    callback.onError("Erro ao processar dados do usuário.");
                }
            } else {
                callback.onCredenciaisInvalidas();
            }
        }, error -> {
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 404) {
                callback.onCredenciaisInvalidas();
                return;
            }
            String errorMessage = "Erro de rede ao tentar fazer login.";
            if (error != null && error.getMessage() != null) {
                errorMessage += ": " + error.getMessage();
            }
            Log.e("UsuarioApiClient", errorMessage, error);
            callback.onError(errorMessage);
        });
        addToRequestQueue(jsonArrayRequest);
    }

    public void editarUsuario(final Usuario usuario, final UpdateUserCallback callback) {
        String url = Constants.BASE_URL + "users/" + usuario.getId();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", usuario.getName());
            jsonBody.put("email", usuario.getEmail());
            jsonBody.put("phone", usuario.getPhone());

            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                jsonBody.put("password", usuario.getPassword());
            }
        } catch (JSONException e) {
            callback.onError("Erro ao criar JSON: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, response -> callback.onSuccess(usuario), error -> {
            String errorMessage = "Erro ao editar usuário.";
            if (error != null) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String body = new String(error.networkResponse.data);
                    errorMessage += " Detalhes: " + body;
                } else if (error.getMessage() != null) {
                    errorMessage += ": " + error.getMessage();
                }
            }
            callback.onError(errorMessage);
        });
        addToRequestQueue(request);
    }

    public void recuperarSenha(String email, String novaSenha, final RecuperarSenhaCallback callback) {
        String url = Constants.BASE_URL + "users?email=" + email;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            if (response.length() > 0) {
                try {
                    JSONObject usuarioJson = response.getJSONObject(0);
                    String userId = usuarioJson.getString("id");

                    String updateUrl = Constants.BASE_URL + "users/" + userId;

                    JSONObject updateBody = new JSONObject();
                    updateBody.put("password", novaSenha);

                    JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PUT, updateUrl, updateBody, updateResponse -> callback.onSuccess(), error -> {
                        String msg = "Erro ao atualizar senha";
                        if (error.getMessage() != null) msg += ": " + error.getMessage();
                        callback.onError(msg);
                    });

                    addToRequestQueue(updateRequest);
                } catch (JSONException e) {
                    callback.onError("Erro ao processar dados do usuário.");
                }
            } else {
                callback.onError("E-mail não encontrado.");
            }
        }, error -> {
            String msg = "Erro ao buscar usuário.";
            if (error.getMessage() != null) msg += ": " + error.getMessage();
            callback.onError(msg);
        });

        addToRequestQueue(request);
    }

    public void obterUsuarioPorId(String id, final UsuarioPorIdCallback callback) {
        String url = Constants.BASE_URL + "users/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Usuario usuario = parseUsuarioFromJson(response);
                        callback.onSuccess(usuario);
                    } catch (JSONException e) {
                        callback.onError("Erro de parseamento do JSON: " + e.getMessage());
                    }
                } else {
                    callback.onError("Usuário não encontrado ou resposta vazia.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Erro ao obter usuário.";
                if (error != null && error.getMessage() != null) {
                    errorMessage += ": " + error.getMessage();
                }
                callback.onError(errorMessage);
            }

        });

        addToRequestQueue(jsonObjectRequest);
    }

    public void obterUsuarioCompletoPorId(String id, final UsuarioCompletoCallback callback) {
        String url = Constants.BASE_URL + "users/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Usuario usuario = parseUsuarioFromJson(response);
                        JSONArray pedidosArray = response.optJSONArray("pedidos");
                        if (pedidosArray != null) {

                            List<Pedido> pedidos = parsePedidosFromJsonArray(pedidosArray);
                            usuario.setPedidos(pedidos);
                        }
                        JSONArray enderecoArray = response.optJSONArray("enderecos");
                        if (enderecoArray != null) {
                            ArrayList<Endereco> enderecoList = new ArrayList<>();
                            for (int i = 0; i < enderecoArray.length(); i++) {
                                Object item = enderecoArray.get(i);
                                if (item instanceof JSONObject) {
                                    Endereco endereco = parseEnderecoFromJson((JSONObject) item);
                                    enderecoList.add(endereco);
                                } else if (item instanceof String) {
                                    String enderecoStr = (String) item;
                                    Endereco endereco = new Endereco();
                                    endereco.setRua(enderecoStr);
                                    enderecoList.add(endereco);
                                }
                            }
                            usuario.setEnderecos(enderecoList);
                        }

                        callback.onSuccess(usuario);
                    } catch (JSONException e) {
                        Log.e("UsuarioApiClient", "Erro de parseamento do JSON completo: " + e.getMessage(), e);
                        callback.onError("Erro ao processar dados completos do usuário.");
                    }
                } else {
                    callback.onError("Usuário não encontrado ou resposta vazia.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Erro ao obter usuário completo.";
                if (error != null && error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    errorMessage += " Código: " + statusCode;
                    if (error.networkResponse.data != null) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e("UsuarioApiClient", "Resposta de erro: " + responseBody);
                        } catch (Exception e) {
                        }
                    }
                } else if (error != null && error.getMessage() != null) {
                    errorMessage += ": " + error.getMessage();
                }
                Log.e("UsuarioApiClient", errorMessage, error);
                callback.onError(errorMessage);
            }
        });
        addToRequestQueue(jsonObjectRequest);
    }

    public void atualizarUsuarioCompleto(Usuario usuario, final AtualizarUsuarioCompletoCallback callback) {
        String url = Constants.BASE_URL + "users/" + usuario.getId();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id", usuario.getId());
            requestBody.put("nome", usuario.getName());
            requestBody.put("email", usuario.getEmail());
            requestBody.put("telefone", usuario.getPhone());
            requestBody.put("senha", usuario.getPassword());

            if (usuario.getPedidos() != null) {
                JSONArray pedidosArray = new JSONArray();
                for (Object item : usuario.getPedidos()) {
                    if (item instanceof JSONObject) {
                        pedidosArray.put(item);
                    } else if (item instanceof Pedido) {
                        pedidosArray.put(pedidoToJson((Pedido) item));
                    }
                }
                requestBody.put("pedidos", pedidosArray);
            } else {
                requestBody.put("pedidos", new JSONArray());
            }

            if (usuario.getEnderecos() != null) {
                JSONArray enderecoArray = new JSONArray();
                for (Object endereco : usuario.getEnderecos()) {
                    if (endereco instanceof JSONObject) {
                        enderecoArray.put(endereco);
                    } else if (endereco instanceof Endereco) {
                        enderecoArray.put(enderecoToJson((Endereco) endereco));
                    } else {
                        Log.w("UsuarioApiClient", "Item de endereço não é JSONObject nem Endereco.");
                    }
                }
                requestBody.put("enderecos", enderecoArray);
            } else {
                requestBody.put("enderecos", new JSONArray());
            }

        } catch (JSONException e) {
            Log.e("UsuarioApiClient", "Erro ao preparar JSON para atualização completa do usuário: " + e.getMessage(), e);
            callback.onError("Erro ao preparar dados para atualização.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Usuario usuarioAtualizado = parseUsuarioFromJson(response);

                    JSONArray pedidosArray = response.optJSONArray("pedidos");
                    if (pedidosArray != null) {
                        List<Pedido> pedidos = parsePedidosFromJsonArray(pedidosArray);
                        usuarioAtualizado.setPedidos(pedidos);
                    }

                    JSONArray enderecoArray = response.optJSONArray("enderecos");
                    if (enderecoArray != null) {
                        ArrayList<Endereco> enderecoList = new ArrayList<>();
                        for (int i = 0; i < enderecoArray.length(); i++) {
                            JSONObject enderecoJson = enderecoArray.getJSONObject(i);
                            Endereco endereco = new Endereco(enderecoJson.optString("rua"), enderecoJson.optString("numero"), enderecoJson.optString("complemento"), enderecoJson.optString("bairro"), enderecoJson.optString("cidade"), enderecoJson.optString("estado"), enderecoJson.optString("cep"));
                            enderecoList.add(endereco);
                        }
                        usuarioAtualizado.setEnderecos(enderecoList);
                    }

                    callback.onSuccess(usuarioAtualizado);
                } catch (JSONException e) {
                    Log.e("UsuarioApiClient", "Erro ao parsear resposta de atualização completa: " + e.getMessage(), e);
                    callback.onError("Erro ao processar resposta de atualização.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Erro ao atualizar usuário completo.";
                if (error != null && error.networkResponse != null) {
                    errorMessage += " Status: " + error.networkResponse.statusCode;
                    if (error.networkResponse.data != null) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e("UsuarioApiClient", "Resposta de erro no PUT: " + responseBody);
                        } catch (Exception e) {
                        }
                    }
                } else if (error != null && error.getMessage() != null) {
                    errorMessage += ": " + error.getMessage();
                }
                Log.e("UsuarioApiClient", errorMessage, error);
                callback.onError(errorMessage);
            }
        });
        addToRequestQueue(jsonObjectRequest);
    }

    public void salvarNovoPedido(Pedido pedido, final SalvarPedidoCallback callback) {
        Usuario usuario = getUsuarioLogado();

        if (usuario == null) {
            callback.onError("Usuário não está logado.");
            return;
        }

        obterUsuarioCompletoPorId(usuario.getId(), new UsuarioCompletoCallback() {
            @Override
            public void onSuccess(Usuario usuarioAtualizado) {
                setUsuarioLogado(usuarioAtualizado);

                if (usuarioAtualizado.getPedidos() == null) {
                    usuarioAtualizado.setPedidos(new ArrayList<>());
                }

                usuarioAtualizado.getPedidos().add(pedido);

                atualizarUsuarioCompleto(usuarioAtualizado, new AtualizarUsuarioCompletoCallback() {
                    @Override
                    public void onSuccess(Usuario usuarioComPedidoSalvo) {
                        setUsuarioLogado(usuarioComPedidoSalvo);
                        callback.onSuccess(usuarioComPedidoSalvo);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError("Erro ao salvar o pedido: " + errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError("Erro ao obter dados do usuário antes de salvar o pedido: " + errorMessage);
            }
        });
    }

    private JSONObject pedidoToJson(Pedido pedido) throws JSONException {
        JSONObject pedidoJson = new JSONObject();
        pedidoJson.put("id", pedido.getId());
        pedidoJson.put("total", pedido.getTotal());
        pedidoJson.put("metodoPagamento", pedido.getMetodoPagamento());
        pedidoJson.put("dataDoPedido", dateFormat.format(pedido.getDataDoPedido()));

        JSONArray itensJsonArray = new JSONArray();
        for (CarrinhoItem item : pedido.getItens()) {
            if (item.getProduto() != null) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("quantidade", item.getQuantidade());

                JSONObject produtoJson = new JSONObject();
                produtoJson.put("id", item.getProduto().getId());
                produtoJson.put("nome", item.getProduto().getTitulo());
                produtoJson.put("descricao", item.getProduto().getDescricao());
                produtoJson.put("preco", item.getProduto().getPreco());
                produtoJson.put("imageUrl", item.getProduto().getImagem());
                produtoJson.put("categoria", item.getProduto().getCategoria());

                itemJson.put("produto", produtoJson);
                itensJsonArray.put(itemJson);
            } else {
                Log.e("UsuarioApiClient", "Item do pedido com produto nulo. Ignorando este item.");
            }
        }
        pedidoJson.put("itens", itensJsonArray);

        if (pedido.getEnderecoEntrega() != null) {
            Endereco endereco = pedido.getEnderecoEntrega();
            JSONObject enderecoJson = new JSONObject();
            enderecoJson.put("rua", endereco.getRua());
            enderecoJson.put("numero", endereco.getNumero());
            enderecoJson.put("complemento", endereco.getComplemento());
            enderecoJson.put("bairro", endereco.getBairro());
            enderecoJson.put("cidade", endereco.getCidade());
            enderecoJson.put("estado", endereco.getEstado());
            enderecoJson.put("cep", endereco.getCep());

            pedidoJson.put("enderecoEntrega", enderecoJson);
        }

        return pedidoJson;
    }

    private List<Pedido> parsePedidosFromJsonArray(JSONArray pedidosArray) {
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < pedidosArray.length(); i++) {
            try {
                JSONObject pedidoJson = pedidosArray.getJSONObject(i);
                pedidos.add(parsePedidoFromJson(pedidoJson));
            } catch (JSONException e) {
                Log.e("UsuarioApiClient", "Erro ao parsear pedido do JSON.", e);
            }
        }
        return pedidos;
    }

    private Pedido parsePedidoFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("id");
            double total = jsonObject.getDouble("total");
            String metodoPagamento = jsonObject.getString("metodoPagamento");
            String dataDoPedidoString = jsonObject.getString("dataDoPedido");

            Date dataDoPedido = null;
            try {
                dataDoPedido = dateFormat.parse(dataDoPedidoString);
            } catch (ParseException e) {
                Log.e("UsuarioApiClient", "Erro ao parsear a data do pedido: " + dataDoPedidoString, e);
            }

            JSONArray itensJsonArray = jsonObject.getJSONArray("itens");
            List<CarrinhoItem> itens = parseItensCarrinhoFromJsonArray(itensJsonArray);

            Pedido pedido = new Pedido();
            pedido.setId(id);
            pedido.setTotal(total);
            pedido.setMetodoPagamento(metodoPagamento);
            pedido.setDataDoPedido(dataDoPedido);
            pedido.setItens(itens);

            if (jsonObject.has("enderecoEntrega")) {
                JSONObject enderecoJson = jsonObject.getJSONObject("enderecoEntrega");
                Endereco endereco = new Endereco(enderecoJson.optString("rua"), enderecoJson.optString("numero"), enderecoJson.optString("complemento"), enderecoJson.optString("bairro"), enderecoJson.optString("cidade"), enderecoJson.optString("estado"), enderecoJson.optString("cep"));
                pedido.setEnderecoEntrega(endereco);
            }

            return pedido;

        } catch (JSONException e) {
            Log.e("UsuarioApiClient", "Erro ao parsear JSON para Pedido.", e);
            return null;
        }
    }

    private List<CarrinhoItem> parseItensCarrinhoFromJsonArray(JSONArray itensArray) {
        List<CarrinhoItem> itens = new ArrayList<>();
        for (int i = 0; i < itensArray.length(); i++) {
            try {
                JSONObject itemJson = itensArray.getJSONObject(i);
                itens.add(parseCarrinhoItemFromJson(itemJson));
            } catch (JSONException e) {
                Log.e("UsuarioApiClient", "Erro ao parsear item do JSON.", e);
            }
        }
        return itens;
    }

    private CarrinhoItem parseCarrinhoItemFromJson(JSONObject jsonObject) {
        try {
            int quantidade = jsonObject.getInt("quantidade");
            JSONObject produtoJson = jsonObject.getJSONObject("produto");
            CardapioItem produto = parseCardapioItemFromJson(produtoJson);
            return new CarrinhoItem(produto, quantidade);
        } catch (JSONException e) {
            Log.e("UsuarioApiClient", "Erro ao parsear JSON para ItemCarrinho.", e);
            return null;
        }
    }

    private CardapioItem parseCardapioItemFromJson(JSONObject jsonObject) {
        try {
            int id = jsonObject.getInt("id");
            String nome = jsonObject.getString("nome");
            String descricao = jsonObject.getString("descricao");
            String preco = String.valueOf(jsonObject.getDouble("preco"));
            String imageUrl = jsonObject.getString("imageUrl");
            String categoria = jsonObject.getString("categoria");
            String tempo = jsonObject.optString("tempo", "Indefinido");
            boolean disponivel = jsonObject.optBoolean("disponivel", true);
            boolean gluten = jsonObject.optBoolean("gluten", false);

            List<String> ingredientes = new ArrayList<>();
            JSONArray ingredientesJson = jsonObject.optJSONArray("ingredientes");
            if (ingredientesJson != null) {
                for (int i = 0; i < ingredientesJson.length(); i++) {
                    ingredientes.add(ingredientesJson.getString(i));
                }
            }

            List<String> alergenicos = new ArrayList<>();
            JSONArray alergenicosJson = jsonObject.optJSONArray("alergenicos");
            if (alergenicosJson != null) {
                for (int i = 0; i < alergenicosJson.length(); i++) {
                    alergenicos.add(alergenicosJson.getString(i));
                }
            }

            return new CardapioItem(id, imageUrl, nome, descricao, preco, tempo, disponivel, gluten, categoria, ingredientes, alergenicos);
        } catch (JSONException e) {
            Log.e("UsuarioApiClient", "Erro ao parsear JSON para Produto.", e);
            return null;
        }
    }

    private JSONObject enderecoToJson(Endereco endereco) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("rua", endereco.getRua());
        json.put("numero", endereco.getNumero());
        json.put("complemento", endereco.getComplemento());
        json.put("bairro", endereco.getBairro());
        json.put("cidade", endereco.getCidade());
        json.put("estado", endereco.getEstado());
        json.put("cep", endereco.getCep());
        return json;
    }

    private Endereco parseEnderecoFromJson(JSONObject json) throws JSONException {
        return new Endereco(json.optString("rua"), json.optString("numero"), json.optString("complemento"), json.optString("bairro"), json.optString("cidade"), json.optString("estado"), json.optString("cep"));
    }

    private Usuario parseUsuarioFromJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String nome = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String telefone = jsonObject.getString("phone");
        String senha = jsonObject.getString("password");

        Usuario usuario = new Usuario(nome, email, telefone, senha);
        usuario.setId(id);
        return usuario;
    }

}

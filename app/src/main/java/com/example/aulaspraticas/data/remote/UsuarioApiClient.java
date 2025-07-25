package com.example.aulaspraticas.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aulaspraticas.model.Usuario;
import com.example.aulaspraticas.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioApiClient {
    private static UsuarioApiClient instance;
    private RequestQueue requestQueue;

    private UsuarioApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized UsuarioApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioApiClient(context);
        }
        return instance;
    }

    private void addToRequestQueue(Request<?> request) {
        requestQueue.add(request);
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

    public void verificarEmailUnico(String email, final EmailCheckCallback callback) {
        String url = Constants.BASE_URL + "users?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> callback.onSuccess(response.length() == 0),
                error -> {
                    String errorMessage = "Erro ao verificar unicidade do e-mail.";
                    if (error != null && error.getMessage() != null) {
                        errorMessage += ": " + error.getMessage();
                    }
                    Log.e("UsuarioApiClient", errorMessage, error);
                    callback.onError(errorMessage);
                }
        );
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        String id = response.getString("id");
                        usuario.setId(id);
                        callback.onSuccess(usuario);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Erro ao processar resposta de cadastro.");
                    }
                },
                error -> {
                    String errorMessage = "Erro ao cadastrar usuário.";
                    if (error != null && error.networkResponse != null) {
                        errorMessage += " Status: " + error.networkResponse.statusCode;
                    } else if (error != null && error.getMessage() != null) {
                        errorMessage += ": " + error.getMessage();
                    }
                    Log.e("UsuarioApiClient", errorMessage, error);
                    callback.onError(errorMessage);
                }
        );
        addToRequestQueue(jsonObjectRequest);
    }

    public void autenticarUsuario(String email, String senha, final LoginCallback callback) {
        String url = Constants.BASE_URL + "users?email=" + email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
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
                },
                error -> {
                    if (error != null && error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 404) {
                            callback.onCredenciaisInvalidas();
                            return;
                        }
                    }
                    String errorMessage = "Erro de rede ao tentar fazer login.";
                    if (error != null && error.getMessage() != null) {
                        errorMessage += ": " + error.getMessage();
                    }
                    Log.e("UsuarioApiClient", errorMessage, error);
                    callback.onError(errorMessage);
                }
        );
        addToRequestQueue(jsonArrayRequest);
    }
}

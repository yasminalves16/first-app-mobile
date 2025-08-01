package com.example.aulaspraticas.data.remote;

import android.content.Context;
import android.widget.Toast;

import com.example.aulaspraticas.model.CarrinhoItem;
import com.example.aulaspraticas.model.CardapioItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarrinhoSingleton {
    private static CarrinhoSingleton instance;
    private List<CarrinhoItem> itens;
    private List<OnCartChangeListener> listeners;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private CarrinhoSingleton() {
        itens = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public static synchronized CarrinhoSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new CarrinhoSingleton();
        }
        return instance;
    }

    public void registerListener(OnCartChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(OnCartChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners() {
        for (OnCartChangeListener listener : listeners) {
            listener.onCartChanged();
        }
    }

    public void adicionarProduto(CardapioItem produto, int quantidade) {
        for (CarrinhoItem item : itens) {
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                notifyListeners();
                return;
            }
        }
        itens.add(new CarrinhoItem(produto, quantidade));
        notifyListeners();
    }

    public void adicionarProduto(CardapioItem produto) {
        adicionarProduto(produto, 1);
    }


    public void removerQuantidade(CardapioItem produto, int quantidade) {
        Iterator<CarrinhoItem> iterator = itens.iterator();
        while (iterator.hasNext()) {
            CarrinhoItem item = iterator.next();
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(item.getQuantidade() - quantidade);
                if (item.getQuantidade() <= 0) {
                    iterator.remove();
                }
                notifyListeners();
                return;
            }
        }
    }

    public void removerProduto(CardapioItem produto) {
        Iterator<CarrinhoItem> iterator = itens.iterator();
        while (iterator.hasNext()) {
            CarrinhoItem item = iterator.next();
            if (item.getProduto().equals(produto)) {
                iterator.remove();
                notifyListeners();
                return;
            }
        }
    }

    public void limparCarrinho() {
        itens.clear();
        notifyListeners();
    }

    public List<CarrinhoItem> getItens() {
        return new ArrayList<>(itens);
    }

    public double calcularTotal() {
        double total = 0.0;
        for (CarrinhoItem item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }
}

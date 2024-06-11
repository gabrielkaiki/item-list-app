package com.gabrielkaiki.itemlistapp.Modelo;

import java.io.Serializable;

public class Item implements Serializable {

    private String nome, data, id;
    private Double preco;
    private int quantidade;

    public Item() {
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String item) {
        this.nome = item;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}

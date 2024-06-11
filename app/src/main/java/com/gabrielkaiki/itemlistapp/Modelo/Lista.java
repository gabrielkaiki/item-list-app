package com.gabrielkaiki.itemlistapp.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lista implements Serializable {
    private String nome, data, id;
    private List<Item> itens = new ArrayList<>();

    public Lista() {
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}

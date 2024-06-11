package com.gabrielkaiki.itemlistapp.Interfaces;

import com.gabrielkaiki.itemlistapp.Modelo.Lista;

import java.util.List;

public interface iListaDAO {

    boolean salvar(Lista lista);

    boolean deletar(Lista lista);

    boolean atualizar(Lista lista);

    List<Lista> listar();
}

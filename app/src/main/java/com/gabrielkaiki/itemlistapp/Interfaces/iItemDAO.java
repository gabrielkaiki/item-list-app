package com.gabrielkaiki.itemlistapp.Interfaces;

import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;

import java.util.List;

public interface iItemDAO {
    boolean salvar(Item item);

    boolean deletar(Item item);

    boolean atualizar(Item item);

    List<Item> listar(Lista lista);
}

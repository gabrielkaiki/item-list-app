package com.gabrielkaiki.itemlistapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.gabrielkaiki.itemlistapp.Helper.BancoDados;
import com.gabrielkaiki.itemlistapp.Helper.VariaveisGlobais;
import com.gabrielkaiki.itemlistapp.Interfaces.iItemDAO;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements iItemDAO {
    private SQLiteDatabase escreve;
    private SQLiteDatabase le;
    private Context contexto;

    public ItemDAO(Context context) {
        BancoDados banco = new BancoDados(context);
        this.escreve = banco.getWritableDatabase();
        this.le = banco.getReadableDatabase();
        this.contexto = context;
    }

    @Override
    public boolean salvar(Item item) {
        ContentValues cv = new ContentValues();
        cv.put("item", item.getNome());
        cv.put("quantidade", item.getQuantidade());
        cv.put("preco", item.getPreco());
        try {
            escreve.insert(VariaveisGlobais.listaAtual.getNome(), null, cv);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletar(Item item) {
        String[] args = {item.getId()};
        try {
            escreve.delete(VariaveisGlobais.listaAtual.getNome(), "id = ?", args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean atualizar(Item item) {
        ContentValues cv = new ContentValues();
        cv.put("item", item.getNome());
        cv.put("quantidade", item.getQuantidade());
        cv.put("preco", item.getPreco());

        String[] args = {item.getId()};
        try {
            escreve.update(VariaveisGlobais.listaAtual.getNome(), cv, "id = ?", args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Item> listar(Lista list) {
        List<Item> lista = new ArrayList<>();
        String nomeTabela = list.getNome().replaceAll(" ", "");
        try {
            Cursor cursor = le.rawQuery("SELECT * FROM " + nomeTabela, null);

            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setId(String.valueOf(cursor.getInt(0)));
                item.setNome(cursor.getString(1));
                item.setQuantidade(cursor.getInt(2));
                item.setPreco(cursor.getDouble(3));

                lista.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

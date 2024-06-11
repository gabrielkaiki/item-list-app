package com.gabrielkaiki.itemlistapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.gabrielkaiki.itemlistapp.Helper.BancoDados;
import com.gabrielkaiki.itemlistapp.Interfaces.iListaDAO;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;

import java.util.ArrayList;
import java.util.List;

public class ListaDAO implements iListaDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;
    private Context contexto;

    public ListaDAO(Context context) {
        BancoDados banco = new BancoDados(context);
        contexto = context;
        escreve = banco.getWritableDatabase();
        le = banco.getReadableDatabase();
    }

    @Override
    public boolean salvar(Lista lista) {
        ContentValues cv = new ContentValues();
        cv.put("lista", lista.getNome());
        try {
            escreve.insert("listas", null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Lista lista) {
        String[] args = {lista.getId()};
        try {
            escreve.delete("listas", "id = ?", args);
            escreve.execSQL("DROP TABLE " + lista.getNome());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean atualizar(Lista lista) {
        ContentValues cv = new ContentValues();
        cv.put("lista", lista.getNome());

        String[] args = {lista.getId()};
        try {
            escreve.update("listas", cv, "id = ?", args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Lista> listar() {
        List<Lista> lista = new ArrayList<>();
        Cursor cursor = le.rawQuery("SELECT * FROM listas;", null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String l = cursor.getString(1);

            Lista listaObj = new Lista();
            listaObj.setId(id);
            listaObj.setNome(l);

            lista.add(listaObj);
        }
        return lista;
    }
}

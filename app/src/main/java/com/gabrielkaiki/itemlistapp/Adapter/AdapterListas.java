package com.gabrielkaiki.itemlistapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabrielkaiki.itemlistapp.Modelo.Lista;
import com.gabrielkaiki.itemlistapp.R;

import java.util.List;

public class AdapterListas extends RecyclerView.Adapter<AdapterListas.ViewHolder> {
    private List<Lista> listas;

    public AdapterListas(List<Lista> listas) {
        this.listas = listas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_layout, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lista lista = listas.get(position);
        holder.listas.setText(lista.getNome());
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView listas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listas = itemView.findViewById(R.id.textAdapterLista);
        }
    }
}

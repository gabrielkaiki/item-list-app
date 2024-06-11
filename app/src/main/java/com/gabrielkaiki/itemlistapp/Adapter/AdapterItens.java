package com.gabrielkaiki.itemlistapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.R;

import java.util.List;

public class AdapterItens extends RecyclerView.Adapter<AdapterItens.ViewHolder> {

    private List<Item> listaItens;

    public AdapterItens(List<Item> listaItens) {
        this.listaItens = listaItens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_layout, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = listaItens.get(position);
        holder.nome.setText(item.getNome());
        holder.preco.setText("R$ " + item.getPreco());
        holder.qtd.setText("x" + item.getQuantidade());
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, preco, qtd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeItem);
            preco = itemView.findViewById(R.id.textPrecoItem);
            qtd = itemView.findViewById(R.id.textQtdItem);
        }
    }
}

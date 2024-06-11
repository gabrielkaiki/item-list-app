package com.gabrielkaiki.itemlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabrielkaiki.itemlistapp.Adapter.AdapterItens;
import com.gabrielkaiki.itemlistapp.DAO.ItemDAO;
import com.gabrielkaiki.itemlistapp.DAO.ListaDAO;
import com.gabrielkaiki.itemlistapp.Helper.RecyclerItemClickListener;
import com.gabrielkaiki.itemlistapp.Helper.VariaveisGlobais;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;
import com.gabrielkaiki.itemlistapp.databinding.FragmentSecondBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RecyclerView recyclerViewItens;
    private AdapterItens adapterItens;
    private List<Item> listaItens = new ArrayList<>();
    private Lista lista;
    private TextView total;
    private ItemDAO itemDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        VariaveisGlobais.fragmentoAtual = this;

        //Banner
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Inicializar componentes
        inicializarComponentes();

        //Configurar RecyclerView
        configuraRecyclerView();

        //Swipe
        swipe();

        return binding.getRoot();
    }

    private void configuraToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(VariaveisGlobais.listaAtual.getNome());
    }

    private void configuraRecyclerView() {
        adapterItens = new AdapterItens(listaItens);
        recyclerViewItens.setHasFixedSize(true);
        recyclerViewItens.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewItens.setAdapter(adapterItens);
        defineEventoDeCliqueRecycler();
    }

    public void swipe() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                alertDialogExcluir(viewHolder);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerViewItens);
    }

    private void alertDialogExcluir(RecyclerView.ViewHolder viewHolder) {
        Item item = listaItens.get(viewHolder.getAdapterPosition());
        AlertDialog.Builder construtor = new AlertDialog.Builder(getContext()).setCancelable(false).setTitle("Tem certeza que deseja deletar o item: " + item.getNome() + "?");

        construtor.setPositiveButton("Sim", (dialogInterface, i) -> {
            excluirItem(viewHolder);
        });

        construtor.setNegativeButton("Não", (dialogInterface, i) -> {
            adapterItens.notifyDataSetChanged();
        });

        AlertDialog dialog = construtor.create();
        dialog.show();
    }

    private void excluirItem(RecyclerView.ViewHolder viewHolder) {
        int posicao = viewHolder.getAdapterPosition();
        Item item = listaItens.get(posicao);
        ItemDAO itemDAO = new ItemDAO(getContext());
        if (itemDAO.deletar(item)) {
            Toast.makeText(getContext(), "Item removido com sucesso!", Toast.LENGTH_SHORT).show();
            listaItens.remove(posicao);
            if (listaItens.size() == 0) {
                binding.textVazioItens.setVisibility(View.VISIBLE);
                total.setVisibility(View.GONE);
            }
            adapterItens.notifyItemRemoved(posicao);
            calcularTotal();
        } else {
            Toast.makeText(getContext(), "Erro ao deletar item!", Toast.LENGTH_SHORT).show();
        }
    }


    private void inicializarComponentes() {
        recyclerViewItens = binding.recyclerItens;
        lista = VariaveisGlobais.listaAtual;
        total = binding.textTotal;
        itemDAO = new ItemDAO(getContext());
    }

    private void listarItens() {
        listaItens = itemDAO.listar(lista);
        if (listaItens.size() > 0) {
            binding.textVazioItens.setVisibility(View.GONE);
            total.setVisibility(View.VISIBLE);
        }
        calcularTotal();
        configuraRecyclerView();
    }

    private void calcularTotal() {
        Double totalLocal = 0.0;
        for (Item item : listaItens) {
            totalLocal += item.getPreco() * item.getQuantidade();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        total.setText("Total R$ " + decimalFormat.format(totalLocal));
    }

    private boolean controleClickLongo = false;

    private void defineEventoDeCliqueRecycler() {
        recyclerViewItens.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewItens, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onLongItemClick(View view, int position) {
                if (!controleClickLongo) {
                    Item itemSelecionado = listaItens.get(position);
                    Intent intent = new Intent(getContext(), EdicaoItemActivity.class);
                    intent.putExtra("itemSelecionado", itemSelecionado);
                    startActivity(intent);
                    controleClickLongo = true;
                }
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        listarItens();
        controleClickLongo = false;
        configuraToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
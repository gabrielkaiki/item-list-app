package com.gabrielkaiki.itemlistapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.gabrielkaiki.itemlistapp.Adapter.AdapterListas;
import com.gabrielkaiki.itemlistapp.DAO.ListaDAO;
import com.gabrielkaiki.itemlistapp.Helper.BancoDados;
import com.gabrielkaiki.itemlistapp.Helper.RecyclerItemClickListener;
import com.gabrielkaiki.itemlistapp.Helper.VariaveisGlobais;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;
import com.gabrielkaiki.itemlistapp.databinding.FragmentFirstBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecyclerView recyclerListas;
    private AdapterListas adapterListas;
    private List<Lista> listaListas = new ArrayList<>();
    private BancoDados banco;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        VariaveisGlobais.fragmentoAtual = this;

        //Banner
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Inicializar componenetes()
        inicializarComponentes();

        //Configurar recyclerView
        configurarRecycler();

        //Swipe
        swipe();

        //Click Listener recyclerView
        defineEventoDeCliqueRecycler();

        return binding.getRoot();

    }

    private void configurarRecycler() {
        adapterListas = new AdapterListas(listaListas);
        recyclerListas.setHasFixedSize(true);
        recyclerListas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerListas.setAdapter(adapterListas);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void inicializarComponentes() {
        recyclerListas = binding.recyclerListas;
        banco = new BancoDados(getContext());
    }

    private void recuperarListas() {
        ListaDAO listaDAO = new ListaDAO(requireContext());
        listaListas.clear();
        listaListas = listaDAO.listar();
        if (listaListas.size() > 0) {
            binding.textVazioLista.setVisibility(View.GONE);
        }
        configurarRecycler();
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

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerListas);
    }

    private void alertDialogExcluir(RecyclerView.ViewHolder viewHolder) {
        Lista lista = listaListas.get(viewHolder.getAdapterPosition());
        AlertDialog.Builder construtor = new AlertDialog.Builder(getContext()).setCancelable(false).setTitle("Tem certeza que deseja remover a lista: " + lista.getNome() + "?");

        construtor.setPositiveButton("Sim", (dialogInterface, i) -> {
            excluirLista(viewHolder);
        });

        construtor.setNegativeButton("Não", (dialogInterface, i) -> {
            adapterListas.notifyDataSetChanged();
        });

        AlertDialog dialog = construtor.create();
        dialog.show();
    }

    private void excluirLista(RecyclerView.ViewHolder viewHolder) {
        int posicao = viewHolder.getAdapterPosition();
        Lista lista = listaListas.get(posicao);
        ListaDAO listaDAO = new ListaDAO(getContext());
        if (listaDAO.deletar(lista)) {
            Toast.makeText(getContext(), "Lista removida com sucesso!", Toast.LENGTH_SHORT).show();
            listaListas.remove(posicao);
            if (listaListas.size() == 0) {
                binding.textVazioLista.setVisibility(View.VISIBLE);
            }
            adapterListas.notifyItemRemoved(posicao);
        } else {
            Toast.makeText(getContext(), "Erro ao deletar lista!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean controleClickLongo = false;

    private void defineEventoDeCliqueRecycler() {
        recyclerListas.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerListas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
                VariaveisGlobais.listaAtual = listaListas.get(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                if (!controleClickLongo) {
                    Lista listaSelecionada = listaListas.get(position);
                    Intent intent = new Intent(getContext(), EdicaoListaActivity.class);
                    intent.putExtra("listaSelecionada", listaSelecionada);
                    startActivity(intent);
                    controleClickLongo = true;
                }
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarListas();
        controleClickLongo = false;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Listas");
    }
}
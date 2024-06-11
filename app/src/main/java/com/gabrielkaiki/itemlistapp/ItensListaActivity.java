package com.gabrielkaiki.itemlistapp;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gabrielkaiki.itemlistapp.DAO.ItemDAO;
import com.gabrielkaiki.itemlistapp.DAO.ListaDAO;
import com.gabrielkaiki.itemlistapp.Helper.BancoDados;
import com.gabrielkaiki.itemlistapp.Helper.VariaveisGlobais;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;
import com.gabrielkaiki.itemlistapp.databinding.ActivityItensListaBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ItensListaActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityItensListaBinding binding;
    private BancoDados bancoDados;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityItensListaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Ads
        MobileAds.initialize(this, initializationStatus -> {
        });

        //Inicializar componenetes
        inicializarComponentes();

        //Toolbar
        toolbar = binding.icludeToolbar.toolbar;
        toolbar.setTitle("Listas");
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_itens_lista);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VariaveisGlobais.fragmentoAtual instanceof FirstFragment) {
                    alertDialogListas();
                } else {
                    alertDialogItens();
                }
            }
        });
    }

    private void alertDialogItens() {
        View viewItem = getLayoutInflater().inflate(R.layout.alertdialog_add_item, null);
        AlertDialog.Builder construtor = new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Configuração do item").setView(viewItem);

        construtor.setPositiveButton("Salvar", (dialogInterface, i) -> {
            if (validaCamposItem(viewItem)) {
                salvarItem(viewItem);
            }
        });

        construtor.setNegativeButton("Fechar", (dialogInterface, i) -> {

        });

        AlertDialog dialog = construtor.create();
        dialog.show();
    }

    private void salvarItem(View viewItem) {
        TextView nomeItem = viewItem.findViewById(R.id.textNomeItem);
        TextView precoItem = viewItem.findViewById(R.id.textPrecoItem);
        TextView qtdItem = viewItem.findViewById(R.id.textQtdItem);

        String nome = nomeItem.getText().toString();
        Double preco = Double.parseDouble(precoItem.getText().toString());
        int quantidade = Integer.parseInt(qtdItem.getText().toString());

        Item item = new Item();
        item.setNome(nome);
        item.setPreco(preco);
        item.setQuantidade(quantidade);

        ItemDAO itemDAO = new ItemDAO(this);
        if (itemDAO.salvar(item)) {
            Toast.makeText(this, "Item salvo com sucesso!", Toast.LENGTH_SHORT).show();
            atualizarRecyclerViewAtual();
        } else {
            Toast.makeText(this, "Erro ao salvar item!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validaCamposItem(View viewItem) {
        TextView nomeItem = viewItem.findViewById(R.id.textNomeItem);
        TextView precoItem = viewItem.findViewById(R.id.textPrecoItem);
        TextView qtdItem = viewItem.findViewById(R.id.textQtdItem);

        if (nomeItem.getText().toString() != null) {
            if (precoItem.getText().toString() != null) {
                if (qtdItem.getText().toString() != null) {
                    return true;
                } else {
                    Toast.makeText(this, "Por favor, informe a quantidade!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, informe o preço do item!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, informe o nome do item!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void inicializarComponentes() {
        bancoDados = new BancoDados(getApplicationContext());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_itens_lista);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


    private void alertDialogListas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false).setTitle("Configuração da lista");

        View layout = getLayoutInflater().inflate(R.layout.alertdialog_add_lista, null);
        builder.setView(layout);

        builder.setPositiveButton("Salvar", null);
        builder.setNegativeButton("Fechar", (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button botao = dialog.getButton(BUTTON_POSITIVE);
            botao.setOnClickListener(view -> {
                EditText lista = layout.findViewById(R.id.textoItem);
                String listaString = lista.getText().toString().replaceAll(" ", "");

                if (!listaString.isEmpty()) {
                    criaLista(listaString);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Por favor, informe o nome da lista!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }

    private void criaLista(String listaString) {
        bancoDados.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + listaString + " (id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, quantidade INTEGER, preco DECIMAL(18, 2));");
        ListaDAO listaDAO = new ListaDAO(this);

        Lista lista = new Lista();
        lista.setNome(listaString);
        if (listaDAO.salvar(lista)) {
            Toast.makeText(this, "Lista criada com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao criar lista!", Toast.LENGTH_SHORT).show();
        }
        atualizarRecyclerViewAtual();
    }

    public void atualizarRecyclerViewAtual() {
        Fragment fragmentoAtual = VariaveisGlobais.fragmentoAtual;
        if (fragmentoAtual instanceof FirstFragment) {
            NavHostFragment.findNavController(fragmentoAtual).navigate(R.id.action_refresh_firstFragment);
        } else {
            NavController navController = NavHostFragment.findNavController(fragmentoAtual);
            navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
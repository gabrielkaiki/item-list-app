package com.gabrielkaiki.itemlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.gabrielkaiki.itemlistapp.DAO.ListaDAO;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.Modelo.Lista;
import com.gabrielkaiki.itemlistapp.databinding.ActivityEdicaoListaBinding;

public class EdicaoListaActivity extends AppCompatActivity {

    private ActivityEdicaoListaBinding binding;
    private EditText campoNomeLista;
    private Lista listaSelecionada;
    private Menu meuMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEdicaoListaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar
        Toolbar toolbar = binding.includeToolbar.toolbar;
        toolbar.setTitle("Edição de lista");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Componentes
        iniciaComponentes();

        //Obter lista 
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            listaSelecionada = (Lista) bundle.getSerializable("listaSelecionada");
            campoNomeLista.setText(listaSelecionada.getNome());
        }
    }

    private void iniciaComponentes() {
        campoNomeLista = binding.textEdicaoLista;

    }

    private void addTextListener() {
        campoNomeLista.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                comparaTextos();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void comparaTextos() {
        String nomeLista = listaSelecionada.getNome();

        String listaCampo = campoNomeLista.getText().toString();


        if (nomeLista.equals(listaCampo)) {
            meuMenu.getItem(0).setVisible(false);
        } else {
            meuMenu.getItem(0).setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        meuMenu = menu;
        addTextListener();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_salvar:
                salvarAtualizacaoLista();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvarAtualizacaoLista() {
        String nomeLista = campoNomeLista.getText().toString();

        if (!nomeLista.isEmpty()) {
            Lista lista = new Lista();
            lista.setNome(nomeLista);
            lista.setId(listaSelecionada.getId());

            ListaDAO listaDAO = new ListaDAO(this);
            if (listaDAO.atualizar(lista)) {
                Toast.makeText(this, "Lista atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar a lista!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
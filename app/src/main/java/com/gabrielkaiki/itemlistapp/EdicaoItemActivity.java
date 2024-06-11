package com.gabrielkaiki.itemlistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.gabrielkaiki.itemlistapp.DAO.ItemDAO;
import com.gabrielkaiki.itemlistapp.Modelo.Item;
import com.gabrielkaiki.itemlistapp.databinding.ActivityEdicaoItemBinding;

public class EdicaoItemActivity extends AppCompatActivity {

    private ActivityEdicaoItemBinding binding;
    private EditText campoNome;
    private EditText campoPreco;
    private EditText campoQuantidade;
    protected Item itemSelecionado;
    private Menu meuMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEdicaoItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar
        Toolbar toolbar = binding.icludeToolbar.toolbar;
        toolbar.setTitle("Edição de item");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializa componentes
        inicializaComponentes();

        //Recupera item
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemSelecionado = (Item) bundle.getSerializable("itemSelecionado");
            campoNome.setText(itemSelecionado.getNome());
            campoPreco.setText(itemSelecionado.getPreco().toString());
            campoQuantidade.setText(String.valueOf(itemSelecionado.getQuantidade()));
        }
    }

    private void addTextListener() {
        campoPreco.addTextChangedListener(new TextWatcher() {
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

        campoNome.addTextChangedListener(new TextWatcher() {
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

        campoQuantidade.addTextChangedListener(new TextWatcher() {
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
        String nomeItem = itemSelecionado.getNome();
        String precoItem = itemSelecionado.getPreco().toString();
        String quantidadeItem = String.valueOf(itemSelecionado.getQuantidade());

        String nomeCampo = campoNome.getText().toString();
        String precoCampo = campoPreco.getText().toString();
        String qtdCampo = campoQuantidade.getText().toString();

        if (nomeItem.equals(nomeCampo) && precoItem.equals(precoCampo) && quantidadeItem.equals(qtdCampo)) {
            meuMenu.getItem(0).setVisible(false);
        } else {
            meuMenu.getItem(0).setVisible(true);
        }
    }

    private boolean validarCampos() {
        String nome = campoNome.getText().toString();
        String preco = campoPreco.getText().toString();
        String quantidade = campoQuantidade.getText().toString();

        if (!nome.isEmpty()) {
            if (!preco.isEmpty()) {
                if (!quantidade.isEmpty()) {
                    return true;
                } else {
                    Toast.makeText(this, "Por favor, informe a quantidade!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, informe o preço!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, informe o nome do item!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        meuMenu = menu;
        //Listener campos de texto
        addTextListener();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_salvar) atualizarItem();
        return super.onOptionsItemSelected(item);
    }

    private void atualizarItem() {
        if (validarCampos()) {
            String nome = campoNome.getText().toString();
            String preco = campoPreco.getText().toString();
            String quantidade = campoQuantidade.getText().toString();

            Item item = new Item();
            item.setId(itemSelecionado.getId());
            item.setNome(nome);
            item.setPreco(Double.parseDouble(preco));
            item.setQuantidade(Integer.parseInt(quantidade));

            ItemDAO itemDAO = new ItemDAO(this);
            if (itemDAO.atualizar(item)) {
                Toast.makeText(this, "Item atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar item!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void inicializaComponentes() {
        campoNome = binding.textNomeItemEdicao;
        campoPreco = binding.textPrecoItemEdicao;
        campoQuantidade = binding.textQuantidadeItemEdicao;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
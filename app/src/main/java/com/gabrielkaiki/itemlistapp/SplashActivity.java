package com.gabrielkaiki.itemlistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            abrirTelaPrincipal();
        }, 3000);
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(this, ItensListaActivity.class));
        finish();
    }
}
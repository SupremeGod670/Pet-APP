package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class MostrarPetActivity extends AppCompatActivity {

    private TextView voltar;
    private NestedScrollView rolar;
    private ImageView frente;
    private ImageView verso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pet);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarPetActivity.this, MenuPetsActivity.class);
                startActivity(intent);
            }
        });

        rolar = findViewById(R.id.rolar);
        frente = findViewById(R.id.frente);
        verso = findViewById(R.id.verso);

    }
}

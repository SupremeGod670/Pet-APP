package com.example.petapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity.ScreenCaptureCallback;

public class MostrarPetActivity extends AppCompatActivity {

    private TextView voltar;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pet);

        voltar = findViewById(R.id.voltar);
        viewPager = findViewById(R.id.rolar);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarPetActivity.this, MenuPetsActivity.class);
                startActivity(intent);
            }
        });

    }
}

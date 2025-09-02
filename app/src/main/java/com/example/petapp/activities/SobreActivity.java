package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SobreActivity extends AppCompatActivity {

    private TextView textView, textView1, textView2, textView3, voltar;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView; // Mudança aqui
    ImageButton open;
    Button sair;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sobre_app);

        voltar = findViewById(R.id.voltar);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InicioActivity extends AppCompatActivity {

    private LinearLayout meuspets, pets_item, qr_code_item;
    private BottomNavigationView navigationb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        meuspets = findViewById(R.id.meuspets);
        pets_item = findViewById(R.id.pets_item);
        qr_code_item = findViewById(R.id.qr_code_item);

        navigationb = findViewById(R.id.navigationb);

        setupBottomNavigation();

        meuspets.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        pets_item.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        qr_code_item.setOnClickListener(v -> {

        });
    }

    private void setupBottomNavigation() {
        navigationb.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                Toast.makeText(InicioActivity.this, "Inìcio Clicado", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_menu) {
                Intent intent = new Intent(InicioActivity.this, MenuActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_sobre) {
                Intent intent = new Intent(InicioActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_config) {
                Toast.makeText(InicioActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}

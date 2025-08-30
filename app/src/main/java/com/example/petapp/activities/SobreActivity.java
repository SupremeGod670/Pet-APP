package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

    private TextView textView, textView1, textView2, textView3;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView; // Mudança aqui
    ImageButton open;
    Button sair;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sobre_app);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        bottomNavigationView = findViewById(R.id.navigationb); // Mudança aqui

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_inicio) {
                    Intent intent = new Intent(SobreActivity.this, InicioActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_menu) {
                    Intent intent = new Intent(SobreActivity.this, MenuActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_sobre) {
                    Toast.makeText(SobreActivity.this, "Você já está na tela de Sobre", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_config) {
                    Toast.makeText(SobreActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}
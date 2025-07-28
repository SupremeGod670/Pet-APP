package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.R;
import com.google.android.material.navigation.NavigationView;

public class SobreActivity extends AppCompatActivity {

    private TextView textView, textView1, textView2, textView3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton open;
    Button sair;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sobre_menu);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        open = findViewById(R.id.open);
        sair = findViewById(R.id.sair);

        setupNavigationDrawer();
        setupButtonClickListeners();
    }

    private void setupNavigationDrawer() {
        open.setOnClickListener(view -> drawerLayout.open());

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_config) { // Exemplo de outro item
                Toast.makeText(SobreActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_sobre) {
                Toast.makeText(SobreActivity.this, "Você já está na tela de Sobre", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_menu) {
                Intent intent = new Intent(SobreActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }

            drawerLayout.close();
            return true; // Indica que o item foi tratado
        });
    }

    private void setupButtonClickListeners() {
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SobreActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

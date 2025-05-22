package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;
import com.google.android.material.navigation.NavigationView;

import com.example.petapp.LoginActivity;

public class MenuActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton open;
    TextView criar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        criar = findViewById(R.id.criar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        open = findViewById(R.id.open);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.nav_logout){
                    Toast.makeText(MenuActivity.this, "Logout Clicado", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                drawerLayout.close();

                return false;
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CriarPetsActivity.class);
                startActivity(intent);
            }
        });

    }
}

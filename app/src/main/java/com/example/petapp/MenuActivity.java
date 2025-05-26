package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.adapter.CriarPetsModel;
import com.example.petapp.adapter.PetAdapter;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private ListView listpet1, listpet2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button sair;
    ImageButton open;
    TextView criar, nomePet;
    Spinner editraca;
    LinearLayout pet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        criar = findViewById(R.id.criar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        sair = findViewById(R.id.sair);
        open = findViewById(R.id.open);

        listpet1 = findViewById(R.id.listpet1);
        listpet2 = findViewById(R.id.listpet2);

        pet = findViewById(R.id.pet);

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

                if (itemId == R.id.nav_logout) {
                    Toast.makeText(MenuActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();

                }

                drawerLayout.close();

                return false;
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        RegistroPetDAO dao = new RegistroPetDAO(this);

        ArrayList<CriarPetsModel> getPets = new ArrayList<>();
        getPets.add(new CriarPetsModel("", "" + dao.selectNome(), "" + dao.selectRaca()));

        PetAdapter adapter = new PetAdapter(this, getPets);
        listpet1.setAdapter(adapter);

        listpet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);
                startActivity(intent);
            }
        });

        listpet2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);
                startActivity(intent);
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

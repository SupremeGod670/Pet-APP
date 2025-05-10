package com.example.petapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.adapter.CriarPetsModel;
import com.example.petapp.adapter.PetAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MenuPetsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePicker.OnCreateContextMenuListener {

    private SearchView pesquisar;
    private ListView listpet1, listpet2;
    private TextView criar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pets);

        pesquisar = findViewById(R.id.pesquisar);
        listpet1 = findViewById(R.id.listpet1);
        listpet2 = findViewById(R.id.listpet2);
        criar = findViewById(R.id.criar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu Pets");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MenuPetsActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPetsActivity.this, CriarPetsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_logout){
            Intent intent = new Intent(MenuPetsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return true;
    }
}

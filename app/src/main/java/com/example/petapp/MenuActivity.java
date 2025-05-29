package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.adapter.CriarPetsModel;
import com.example.petapp.adapter.PetAdapter;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ListView listpet1, listpet2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button sair;
    ImageButton open;
    private ImageButton criar;
    LinearLayout pet;
    private RegistroPetDAO registroPetDAO;
    private static final int MAX_PETS_IN_LIST1 = 5;

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

        listpet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CriarPetsModel clickedPet = (CriarPetsModel) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);

                intent.putExtra("PET_ID", clickedPet.getId());
                intent.putExtra("PET_NAME", clickedPet.getNome());
                intent.putExtra("PET_RACE", clickedPet.getRaca());

                startActivity(intent);
            }
        });

        listpet2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CriarPetsModel clickedPet = (CriarPetsModel) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);

                intent.putExtra("PET_ID", clickedPet.getId());
                intent.putExtra("PET_NAME", clickedPet.getNome());
                intent.putExtra("PET_RACE", clickedPet.getRaca());

                startActivity(intent);
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, CriarPetsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadAllPetDataAndDistribute() {
        registroPetDAO = new RegistroPetDAO(this);
        List<RegistroPetModel> petsFromDB = registroPetDAO.getAllPets();
        Log.d("MenuActivity", "Total pets loaded from DB: " + petsFromDB.size());

        ArrayList<CriarPetsModel> petsForList1 = new ArrayList<>();
        ArrayList<CriarPetsModel> petsForList2 = new ArrayList<>();

        if (petsFromDB != null && !petsFromDB.isEmpty()) {
            for (int i = 0; i < petsFromDB.size(); i++) {
                RegistroPetModel pet = petsFromDB.get(i);
                CriarPetsModel displayPet = new CriarPetsModel(
                        "", // Placeholder for image URL/resource ID
                        pet.getNomepet(),
                        pet.getRaca(),
                        pet.getId() // Pass the ID if CriarPetsModel is updated to hold it
                );

                if (i < MAX_PETS_IN_LIST1) {
                    // Add to the first list if within the limit
                    petsForList1.add(displayPet);
                    Log.d("MenuActivity", "Adding to listpet1: " + pet.getNomepet());
                } else {
                    // Add remaining pets to the second list
                    petsForList2.add(displayPet);
                    Log.d("MenuActivity", "Adding to listpet2: " + pet.getNomepet());
                }
            }
        } else {
            Toast.makeText(this, "Nenhum pet encontrado.", Toast.LENGTH_SHORT).show();
            Log.d("MenuActivity", "No pets found in database.");
        }

        // Set adapters for both ListViews
        listpet1.setAdapter(new PetAdapter(this, petsForList1));
        listpet2.setAdapter(new PetAdapter(this, petsForList2));

        // Hide list2 if it's empty to avoid showing an empty listview
        if (petsForList2.isEmpty()) {
            listpet2.setVisibility(View.GONE);
        } else {
            listpet2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh both lists every time the activity comes to the foreground
        // This ensures new pets created in CriarPetsActivity are shown correctly
        loadAllPetDataAndDistribute();
    }

}

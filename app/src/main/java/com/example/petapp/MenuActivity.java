package com.example.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.petapp.adapter.PetModel;
import com.example.petapp.adapter.PetAdapter;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private GridView gridView; // Declare GridView
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button sair;
    ImageButton open;
    private ImageButton criar;
    LinearLayout pet;
    private RegistroPetDAO registroPetDAO;
    // private static final int MAX_PETS_IN_LIST1 = 1; // No longer needed for two separate lists

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        criar = findViewById(R.id.criar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        sair = findViewById(R.id.sair);
        open = findViewById(R.id.open);

        gridView = findViewById(R.id.gridview); // Initialize GridView

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

        // Set item click listener for the GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PetModel clickedPet = (PetModel) adapterView.getItemAtPosition(position);
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
                startActivityForResult(intent, 20);
            }
        });

    }

    private void loadAllPetDataAndDistribute() {
        registroPetDAO = new RegistroPetDAO(this);
        List<RegistroPetModel> petsFromDB = registroPetDAO.getAllPets();
        Log.d("MenuActivity", "Total pets loaded from DB: " + petsFromDB.size());

        // Use a single list for all pets to be displayed in the GridView
        ArrayList<PetModel> allPetsForGridView = new ArrayList<>();

        if (petsFromDB != null && !petsFromDB.isEmpty()) {
            for (RegistroPetModel pet : petsFromDB) { // Iterate through all pets
                PetModel displayPet = new PetModel(
                        "", // Placeholder for image URL/resource ID
                        pet.getNomepet(),
                        pet.getRaca(),
                        pet.getId() // Pass the ID if CriarPetsModel is updated to hold it
                );
                allPetsForGridView.add(displayPet); // Add all pets to the single list
                Log.d("MenuActivity", "Adding to GridView: " + pet.getNomepet());
            }
        } else {
            Toast.makeText(this, "Nenhum pet encontrado.", Toast.LENGTH_SHORT).show();
            Log.d("MenuActivity", "No pets found in database.");
        }

        // Set adapter for the GridView
        gridView.setAdapter(new PetAdapter(this, allPetsForGridView));

        // No need to hide/show separate lists anymore
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the GridView every time the activity comes to the foreground
        loadAllPetDataAndDistribute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            // If a new pet was created, refresh the GridView
            loadAllPetDataAndDistribute();
        }
    }
}
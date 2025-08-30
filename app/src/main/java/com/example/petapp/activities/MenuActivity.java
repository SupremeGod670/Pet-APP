package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.adapter.PetAdapter;
import com.example.petapp.adapter.PetModel;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    private GridView gridView;
    BottomNavigationView bottomNavigationView; // Mudança aqui
    private ImageButton criar;
    private RegistroPetDAO registroPetDAO;
    private SearchView pesquisar;
    private ImageView filtro;

    private ArrayList<PetModel> allPetsForGridView = new ArrayList<>();
    private ArrayList<PetModel> currentlyDisplayedPets = new ArrayList<>();

    // Constantes para as opções de filtro no diálogo
    private static final String FILTER_OPTION_NONE = "Nenhum (Padrão)";
    private static final String FILTER_OPTION_ALPHABETICAL = "Ordem Alfabética (Nome)";
    private static final String FILTER_OPTION_RACE = "Filtrar por Raça";
    private static final String RACE_FILTER_ALL_RACES = "Todas as Raças";

    private String currentActiveRaceFilter = RACE_FILTER_ALL_RACES;
    private boolean isAlphabeticalSortActive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pets);

        // Inicialização do DAO uma vez
        registroPetDAO = new RegistroPetDAO(this);

        criar = findViewById(R.id.criar);
        bottomNavigationView = findViewById(R.id.navigationb); // Mudança aqui
        pesquisar = findViewById(R.id.pesquisar);
        filtro = findViewById(R.id.filtro);
        gridView = findViewById(R.id.gridview);

        setupSearchView();
        setupButtonClickListeners();
        setupBottomNavigation(); // Nova função

        // Listener para o botão/ícone de filtro
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_inicio) {
                    Intent intent = new Intent(MenuActivity.this, InicioActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_menu) {
                    Toast.makeText(MenuActivity.this, "Você já está na tela de Menu", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_sobre) {
                    Intent intent = new Intent(MenuActivity.this, SobreActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_config) {
                    Toast.makeText(MenuActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupSearchView() {
        pesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyNameAndCurrentFilters(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyNameAndCurrentFilters(newText);
                return true;
            }
        });

        pesquisar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                applyCurrentFiltersOnly();
                return false;
            }
        });
    }

    private void setupButtonClickListeners() {
        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            PetModel clickedPet = (PetModel) adapterView.getItemAtPosition(position);
            if (clickedPet == null) return;

            Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);
            intent.putExtra("PET_ID", clickedPet.getId());
            intent.putExtra("PET_NAME", clickedPet.getNome());
            intent.putExtra("PET_RACE", clickedPet.getRaca());
            startActivity(intent);
        });

        criar.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CriarPetsActivity.class);
            startActivityForResult(intent, 20);
        });
    }

    private void loadAllPetDataAndInitializeView() {
        List<RegistroPetModel> petsFromDB = registroPetDAO.getAllPets();
        Log.d("MenuActivity", "Total pets loaded from DB: " + (petsFromDB != null ? petsFromDB.size() : "null"));

        allPetsForGridView.clear();

        if (petsFromDB != null && !petsFromDB.isEmpty()) {
            for (RegistroPetModel petDB : petsFromDB) {
                String imageUrl = petDB.getUrlImagem();

                PetModel displayPet = new PetModel(
                        imageUrl != null ? imageUrl : "",
                        petDB.getNomepet(),
                        petDB.getRaca(),
                        petDB.getId()
                );
                allPetsForGridView.add(displayPet);

                Log.d("MenuActivity", "Pet loaded: " + petDB.getNomepet() + ", Image URL: " + imageUrl);
            }
        } else {
            Log.d("MenuActivity", "No pets found in database.");
        }
        applyNameAndCurrentFilters(pesquisar.getQuery().toString());
    }

    private void updateGridView(List<PetModel> petsToDisplay) {
        currentlyDisplayedPets.clear();
        currentlyDisplayedPets.addAll(petsToDisplay);

        PetAdapter adapter = new PetAdapter(this, currentlyDisplayedPets);
        gridView.setAdapter(adapter);

        if (allPetsForGridView.isEmpty()) {
            Toast.makeText(this, "Nenhum pet cadastrado.", Toast.LENGTH_SHORT).show();
        } else if (petsToDisplay.isEmpty() && !pesquisar.getQuery().toString().isEmpty()) {
            Toast.makeText(this, "Nenhum pet encontrado para a pesquisa.", Toast.LENGTH_SHORT).show();
        } else if (petsToDisplay.isEmpty() && (!currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES) || isAlphabeticalSortActive)) {
            Toast.makeText(this, "Nenhum pet encontrado com os filtros aplicados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar e Ordenar Pets");

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 40, 50, 10);

        TextView tvFilterType = new TextView(this);
        tvFilterType.setText("Escolha o tipo de filtro/ordenação:");
        tvFilterType.setPadding(0, 0, 0, 10);
        dialogLayout.addView(tvFilterType);

        Spinner spinnerFilterType = new Spinner(this);
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add(FILTER_OPTION_NONE);
        filterOptions.add(FILTER_OPTION_ALPHABETICAL);
        filterOptions.add(FILTER_OPTION_RACE);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterType.setAdapter(filterAdapter);
        dialogLayout.addView(spinnerFilterType);

        LinearLayout raceFilterLayout = new LinearLayout(this);
        raceFilterLayout.setOrientation(LinearLayout.VERTICAL);
        raceFilterLayout.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        raceFilterLayout.setLayoutParams(params);

        TextView tvRaceLabel = new TextView(this);
        tvRaceLabel.setText("Selecione a Raça:");
        raceFilterLayout.addView(tvRaceLabel);

        Spinner spinnerRaces = new Spinner(this);
        ArrayAdapter<String> raceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaces.setAdapter(raceAdapter);
        raceFilterLayout.addView(spinnerRaces);

        dialogLayout.addView(raceFilterLayout);

        if (isAlphabeticalSortActive && !currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES)) {
            spinnerFilterType.setSelection(filterOptions.indexOf(FILTER_OPTION_RACE));
            raceFilterLayout.setVisibility(View.VISIBLE);
            populateRaceSpinner(raceAdapter, spinnerRaces);
        } else if (isAlphabeticalSortActive) {
            spinnerFilterType.setSelection(filterOptions.indexOf(FILTER_OPTION_ALPHABETICAL));
        } else if (!currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES)) {
            spinnerFilterType.setSelection(filterOptions.indexOf(FILTER_OPTION_RACE));
            raceFilterLayout.setVisibility(View.VISIBLE);
            populateRaceSpinner(raceAdapter, spinnerRaces);
        } else {
            spinnerFilterType.setSelection(filterOptions.indexOf(FILTER_OPTION_NONE));
        }

        spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals(FILTER_OPTION_RACE)) {
                    raceFilterLayout.setVisibility(View.VISIBLE);
                    populateRaceSpinner(raceAdapter, spinnerRaces);
                } else {
                    raceFilterLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                raceFilterLayout.setVisibility(View.GONE);
            }
        });

        builder.setView(dialogLayout);

        builder.setPositiveButton("Aplicar", (dialog, which) -> {
            String selectedFilter = spinnerFilterType.getSelectedItem().toString();

            isAlphabeticalSortActive = false;
            currentActiveRaceFilter = RACE_FILTER_ALL_RACES;

            if (selectedFilter.equals(FILTER_OPTION_ALPHABETICAL)) {
                isAlphabeticalSortActive = true;
            } else if (selectedFilter.equals(FILTER_OPTION_RACE)) {
                if (spinnerRaces.getSelectedItem() != null) {
                    currentActiveRaceFilter = spinnerRaces.getSelectedItem().toString();
                }
            }

            applyNameAndCurrentFilters(pesquisar.getQuery().toString());
        });

        builder.setNegativeButton("Cancelar", null);

        builder.setNeutralButton("Limpar Filtros", (dialog, which) -> {
            isAlphabeticalSortActive = false;
            currentActiveRaceFilter = RACE_FILTER_ALL_RACES;
            pesquisar.setQuery("", false);
            pesquisar.clearFocus();
            pesquisar.setIconified(true);
            loadAllPetDataAndInitializeView();
        });

        builder.create().show();
    }

    private void populateRaceSpinner(ArrayAdapter<String> raceAdapter, Spinner spinnerRaces) {
        Set<String> uniqueRaces = new HashSet<>();
        uniqueRaces.add(RACE_FILTER_ALL_RACES);

        for (PetModel pet : allPetsForGridView) {
            if (pet.getRaca() != null && !pet.getRaca().trim().isEmpty()) {
                uniqueRaces.add(pet.getRaca());
            }
        }

        ArrayList<String> sortedRaces = new ArrayList<>(uniqueRaces);
        Collections.sort(sortedRaces, (r1, r2) -> {
            if (r1.equals(RACE_FILTER_ALL_RACES)) return -1;
            if (r2.equals(RACE_FILTER_ALL_RACES)) return 1;
            return r1.compareToIgnoreCase(r2);
        });

        raceAdapter.clear();
        raceAdapter.addAll(sortedRaces);
        raceAdapter.notifyDataSetChanged();

        if (!currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES) && sortedRaces.contains(currentActiveRaceFilter)) {
            spinnerRaces.setSelection(sortedRaces.indexOf(currentActiveRaceFilter));
        } else {
            spinnerRaces.setSelection(0);
        }
    }

    private void applyCurrentFiltersOnly() {
        applyNameAndCurrentFilters("");
    }

    private void applyNameAndCurrentFilters(String nameQuery) {
        ArrayList<PetModel> petsAfterNameFilter = new ArrayList<>();

        // 1. Filtro por nome (do SearchView)
        if (nameQuery == null || nameQuery.trim().isEmpty()) {
            petsAfterNameFilter.addAll(allPetsForGridView);
        } else {
            String filterPattern = nameQuery.toLowerCase().trim();
            for (PetModel pet : allPetsForGridView) {
                if (pet.getNome() != null && pet.getNome().toLowerCase().contains(filterPattern)) {
                    petsAfterNameFilter.add(pet);
                }
            }
        }

        // 2. Filtro por raça (do diálogo)
        ArrayList<PetModel> petsAfterRaceFilter = new ArrayList<>();
        if (currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES)) {
            petsAfterRaceFilter.addAll(petsAfterNameFilter);
        } else {
            for (PetModel pet : petsAfterNameFilter) {
                if (pet.getRaca() != null && pet.getRaca().equalsIgnoreCase(currentActiveRaceFilter)) {
                    petsAfterRaceFilter.add(pet);
                }
            }
        }

        // 3. Ordenação alfabética (do diálogo)
        if (isAlphabeticalSortActive) {
            Collections.sort(petsAfterRaceFilter, new Comparator<PetModel>() {
                @Override
                public int compare(PetModel p1, PetModel p2) {
                    String name1 = p1.getNome() == null ? "" : p1.getNome();
                    String name2 = p2.getNome() == null ? "" : p2.getNome();
                    return name1.compareToIgnoreCase(name2);
                }
            });
        }

        updateGridView(petsAfterRaceFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllPetDataAndInitializeView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK) {
            // A atualização será feita no onResume()
        }
    }
}
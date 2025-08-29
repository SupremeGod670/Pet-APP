package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.adapter.PetAdapter;
import com.example.petapp.adapter.PetModel;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    private GridView gridView;
    NavigationView navigationView;
    private ImageButton criar;
    // LinearLayout pet; // Parece não estar sendo usado, pode ser removido se for o caso
    private RegistroPetDAO registroPetDAO;
    private SearchView pesquisar;
    private ImageView filtro; // Este é o seu botão/ícone para abrir o diálogo de filtro

    private ArrayList<PetModel> allPetsForGridView = new ArrayList<>();
    private ArrayList<PetModel> currentlyDisplayedPets = new ArrayList<>(); // Para gerenciar a exibição atual

    // Constantes para as opções de filtro no diálogo
    private static final String FILTER_OPTION_NONE = "Nenhum (Padrão)";
    private static final String FILTER_OPTION_ALPHABETICAL = "Ordem Alfabética (Nome)";
    private static final String FILTER_OPTION_RACE = "Filtrar por Raça";
    private static final String RACE_FILTER_ALL_RACES = "Todas as Raças";

    private String currentActiveRaceFilter = RACE_FILTER_ALL_RACES; // Raça atualmente selecionada no filtro
    private boolean isAlphabeticalSortActive = false; // Se a ordenação alfabética está ativa

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pets);

        // Inicialização do DAO uma vez
        registroPetDAO = new RegistroPetDAO(this);

        criar = findViewById(R.id.criar);
        navigationView = findViewById(R.id.navigationb);
        pesquisar = findViewById(R.id.pesquisar);
        filtro = findViewById(R.id.filtro); // Seu botão/ícone de filtro
        gridView = findViewById(R.id.gridview);
        // pet = findViewById(R.id.pet);

        setupSearchView();
        setupButtonClickListeners();

        // Listener para o botão/ícone de filtro
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        // Chamado em onResume, mas uma carga inicial pode ser útil
        // loadAllPetDataAndDistribute(); // Removido daqui, pois onResume cobrirá a carga inicial
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
                // Quando o SearchView é fechado, a query é limpa.
                // Reaplicamos os filtros ativos (raça, alfabético) sem a pesquisa por nome.
                applyCurrentFiltersOnly();
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(item ->

        {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_config) { // Exemplo de outro item
                Toast.makeText(MenuActivity.this, "Configurações Clicado", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_sobre) {
                Intent intent = new Intent(MenuActivity.this, SobreActivity.class);
                startActivity(intent);
                finish();
            } else if (itemId == R.id.nav_menu) {
                Toast.makeText(MenuActivity.this, "Você já está na tela de Menu", Toast.LENGTH_SHORT).show();
            }

            return true; // Indica que o item foi tratado
        });
    }

    private void setupButtonClickListeners() {
        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            PetModel clickedPet = (PetModel) adapterView.getItemAtPosition(position); // Use currentlyDisplayedPets
            if (clickedPet == null) return;

            Intent intent = new Intent(MenuActivity.this, MostrarPetActivity.class);
            intent.putExtra("PET_ID", clickedPet.getId());
            intent.putExtra("PET_NAME", clickedPet.getNome());
            intent.putExtra("PET_RACE", clickedPet.getRaca());
            startActivity(intent);
            // Geralmente não se finaliza a MenuActivity ao ver um pet, para poder voltar.
            // finish();
        });

        criar.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CriarPetsActivity.class);
            startActivityForResult(intent, 20); // Use uma constante para o request code
        });
    }


    private void loadAllPetDataAndInitializeView() {
        // registroPetDAO já inicializado no onCreate
        List<RegistroPetModel> petsFromDB = registroPetDAO.getAllPets();
        Log.d("MenuActivity", "Total pets loaded from DB: " + (petsFromDB != null ? petsFromDB.size() : "null"));

        allPetsForGridView.clear();

        if (petsFromDB != null && !petsFromDB.isEmpty()) {
            for (RegistroPetModel petDB : petsFromDB) {
                // Pass the image URL instead of empty string
                String imageUrl = petDB.getUrlImagem(); // Get the saved image URL

                PetModel displayPet = new PetModel(
                        imageUrl != null ? imageUrl : "", // Use image URL or empty string
                        petDB.getNomepet(),
                        petDB.getRaca(),
                        petDB.getId()
                );
                allPetsForGridView.add(displayPet);

                Log.d("MenuActivity", "Pet loaded: " + petDB.getNomepet() + ", Image URL: " + imageUrl);
            }
        } else {
            // Não mostre Toast aqui, deixe applyCurrentFiltersOnly ou applyNameAndCurrentFilters lidar com a lista vazia
            Log.d("MenuActivity", "No pets found in database.");
        }
        // Após carregar, aplica os filtros e a pesquisa atuais (se houver)
        applyNameAndCurrentFilters(pesquisar.getQuery().toString());
    }

    private void updateGridView(List<PetModel> petsToDisplay) {
        // Atualiza a lista de pets atualmente exibidos
        currentlyDisplayedPets.clear();
        currentlyDisplayedPets.addAll(petsToDisplay);

        PetAdapter adapter = new PetAdapter(this, currentlyDisplayedPets); // Use a lista de exibição atual
        gridView.setAdapter(adapter);

        if (allPetsForGridView.isEmpty()) {
            Toast.makeText(this, "Nenhum pet cadastrado.", Toast.LENGTH_SHORT).show();
        } else if (petsToDisplay.isEmpty() && !pesquisar.getQuery().toString().isEmpty()) {
            Toast.makeText(this, "Nenhum pet encontrado para a pesquisa.", Toast.LENGTH_SHORT).show();
        } else if (petsToDisplay.isEmpty() && (!currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES) || isAlphabeticalSortActive)) {
            Toast.makeText(this, "Nenhum pet encontrado com os filtros aplicados.", Toast.LENGTH_SHORT).show();
        }
    }


    // Mostra o diálogo de filtro
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar e Ordenar Pets");

        // Layout principal do diálogo
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 40, 50, 10); // Padding (left, top, right, bottom)

        // Opção 1: Ordenação Alfabética (como um switch ou checkbox no futuro, por agora um botão de aplicar)
        // Por simplicidade, vamos usar um Spinner para selecionar o tipo de filtro principal

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

        // Layout para o filtro de raça (inicialmente GONE)
        LinearLayout raceFilterLayout = new LinearLayout(this);
        raceFilterLayout.setOrientation(LinearLayout.VERTICAL);
        raceFilterLayout.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0); // top margin
        raceFilterLayout.setLayoutParams(params);


        TextView tvRaceLabel = new TextView(this);
        tvRaceLabel.setText("Selecione a Raça:");
        raceFilterLayout.addView(tvRaceLabel);

        Spinner spinnerRaces = new Spinner(this);
        // As raças serão preenchidas dinamicamente
        ArrayAdapter<String> raceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaces.setAdapter(raceAdapter);
        raceFilterLayout.addView(spinnerRaces);

        dialogLayout.addView(raceFilterLayout);


        // Pré-selecionar opções atuais
        if (isAlphabeticalSortActive && !currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES)) {
            // Se ambos estiverem ativos, priorize mostrar o filtro de raça se ele for específico
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

            isAlphabeticalSortActive = false; // Reset
            currentActiveRaceFilter = RACE_FILTER_ALL_RACES; // Reset

            if (selectedFilter.equals(FILTER_OPTION_ALPHABETICAL)) {
                isAlphabeticalSortActive = true;
            } else if (selectedFilter.equals(FILTER_OPTION_RACE)) {
                if (spinnerRaces.getSelectedItem() != null) {
                    currentActiveRaceFilter = spinnerRaces.getSelectedItem().toString();
                }
            }
            // Se for FILTER_OPTION_NONE, ambos os resets acima já lidaram com isso.

            applyNameAndCurrentFilters(pesquisar.getQuery().toString());
        });

        builder.setNegativeButton("Cancelar", null);

        builder.setNeutralButton("Limpar Filtros", (dialog, which) -> {
            isAlphabeticalSortActive = false;
            currentActiveRaceFilter = RACE_FILTER_ALL_RACES;
            pesquisar.setQuery("", false); // Limpa a pesquisa também
            pesquisar.clearFocus(); // Opcional: remove o foco do SearchView
            pesquisar.setIconified(true); // Opcional: recolhe o SearchView se ele estiver expandido
            loadAllPetDataAndInitializeView(); // Recarrega e mostra todos os pets
        });

        builder.create().show();
    }

    private void populateRaceSpinner(ArrayAdapter<String> raceAdapter, Spinner spinnerRaces) {
        Set<String> uniqueRaces = new HashSet<>();
        uniqueRaces.add(RACE_FILTER_ALL_RACES); // Adiciona a opção "Todas as Raças"

        for (PetModel pet : allPetsForGridView) {
            if (pet.getRaca() != null && !pet.getRaca().trim().isEmpty()) {
                uniqueRaces.add(pet.getRaca());
            }
        }

        ArrayList<String> sortedRaces = new ArrayList<>(uniqueRaces);
        Collections.sort(sortedRaces, (r1, r2) -> {
            if (r1.equals(RACE_FILTER_ALL_RACES)) return -1; // "Todas as Raças" sempre primeiro
            if (r2.equals(RACE_FILTER_ALL_RACES)) return 1;
            return r1.compareToIgnoreCase(r2);
        });


        raceAdapter.clear();
        raceAdapter.addAll(sortedRaces);
        raceAdapter.notifyDataSetChanged();

        // Tenta restaurar a seleção anterior do filtro de raça
        if (!currentActiveRaceFilter.equals(RACE_FILTER_ALL_RACES) && sortedRaces.contains(currentActiveRaceFilter)) {
            spinnerRaces.setSelection(sortedRaces.indexOf(currentActiveRaceFilter));
        } else {
            spinnerRaces.setSelection(0); // Seleciona "Todas as Raças" por padrão
        }
    }

    /**
     * Aplica apenas os filtros de diálogo (alfabético, raça) à lista completa de pets.
     * Usado quando o SearchView é fechado ou quando os filtros do diálogo são alterados.
     */
    private void applyCurrentFiltersOnly() {
        applyNameAndCurrentFilters(""); // Chama com query vazia
    }


    /**
     * Filtra a lista 'allPetsForGridView' com base na query de nome
     * E depois aplica os filtros de diálogo (alfabético, raça).
     */
    private void applyNameAndCurrentFilters(String nameQuery) {
        ArrayList<PetModel> petsAfterNameFilter = new ArrayList<>();

        // 1. Filtro por nome (do SearchView)
        if (nameQuery == null || nameQuery.trim().isEmpty()) {
            petsAfterNameFilter.addAll(allPetsForGridView);
        } else {
            String filterPattern = nameQuery.toLowerCase().trim();
            for (PetModel pet : allPetsForGridView) {
                // Adicione verificação de nulidade para getNome()
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
                // Adicione verificação de nulidade para getRaca()
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
                    // Tratar nomes nulos para evitar NullPointerException
                    String name1 = p1.getNome() == null ? "" : p1.getNome();
                    String name2 = p2.getNome() == null ? "" : p2.getNome();
                    return name1.compareToIgnoreCase(name2);
                }
            });
        }
        // Se a ordenação alfabética não estiver ativa, a ordem atual (do banco ou após filtro de raça) é mantida.
        // Se você quiser uma ordenação padrão (ex: por ID ou data de adição) quando nenhum filtro está ativo, adicione aqui.

        updateGridView(petsAfterRaceFilter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os dados e aplica filtros/pesquisa existentes ao retornar para a activity
        // Isso garante que se um pet foi adicionado/modificado, a lista é atualizada.
        loadAllPetDataAndInitializeView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Se um novo pet foi criado ou modificado (supondo que CriarPetsActivity retorne RESULT_OK)
        if (requestCode == 20 && resultCode == RESULT_OK) {
            // loadAllPetDataAndInitializeView() será chamado no onResume(),
            // então não é estritamente necessário aqui, mas não causa mal.
            // Para garantir a atualização imediata se onResume demorar por algum motivo:
            // loadAllPetDataAndInitializeView();
        }
    }
}
package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.adapter.VeterinarioAdapter;
import com.example.petapp.adapter.VeterinarioModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class VacinacoesActivity extends AppCompatActivity {

    private ListView listViewVeterinarios;
    private VeterinarioAdapter adapter;
    private List<VeterinarioModel> listaCompleta;
    private List<VeterinarioModel> listaFiltrada;
    private EditText editTextPesquisa;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacinacoes);

        initializeViews();
        setupBottomNavigation();
        loadVeterinarios();
        setupSearch();
    }

    private void initializeViews() {
        listViewVeterinarios = findViewById(R.id.listview_veterinarios);
        editTextPesquisa = findViewById(R.id.edit_text_pesquisa);
        bottomNavigationView = findViewById(R.id.navigationb);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_inicio) {
                    Intent intent = new Intent(VacinacoesActivity.this, InicioActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_menu) {
                    Intent intent = new Intent(VacinacoesActivity.this, MenuActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_vacinas) {
                    Toast.makeText(VacinacoesActivity.this, "Você já está na tela de Vacinações", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_config) {
                    Intent intent = new Intent(VacinacoesActivity.this, ConfigActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadVeterinarios() {
        listaCompleta = getVeterinariosData();
        listaFiltrada = new ArrayList<>(listaCompleta);

        adapter = new VeterinarioAdapter(this, listaFiltrada);
        listViewVeterinarios.setAdapter(adapter);
    }

    private void setupSearch() {
        editTextPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarVeterinarios(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrarVeterinarios(String texto) {
        listaFiltrada.clear();

        if (texto.trim().isEmpty()) {
            listaFiltrada.addAll(listaCompleta);
        } else {
            String filtro = texto.toLowerCase().trim();
            for (VeterinarioModel vet : listaCompleta) {
                if (vet.getNome().toLowerCase().contains(filtro) ||
                        vet.getEndereco().toLowerCase().contains(filtro) ||
                        vet.getTipo().toLowerCase().contains(filtro) ||
                        vet.getEspecialidades().toLowerCase().contains(filtro)) {
                    listaFiltrada.add(vet);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (listaFiltrada.isEmpty() && !texto.trim().isEmpty()) {
            Toast.makeText(this, "Nenhum estabelecimento encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private List<VeterinarioModel> getVeterinariosData() {
        List<VeterinarioModel> lista = new ArrayList<>();

        // Dados reais de veterinários em Criciúma-SC
        lista.add(new VeterinarioModel(
                "Clínica Veterinária Cães e Cia",
                "Rua Santa Bárbara, 1450 - Santa Bárbara, Criciúma - SC",
                "(48) 3431-0000",
                "Segunda à Sexta: 8h às 18h | Sábado: 8h às 12h",
                "Vacinação, Consultas, Ortopedia, Medicina Preventiva",
                -28.6773, -49.3695,
                "Clínica Veterinária",
                4.5,
                false
        ));

        lista.add(new VeterinarioModel(
                "SOS Hospital Veterinário",
                "Av. Centenário, 4455 - Centro, Criciúma - SC",
                "(48) 3433-5555",
                "24 horas - Todos os dias",
                "Emergência 24h, Cirurgias, UTI, Internação",
                -28.6778, -49.3692,
                "Hospital Veterinário",
                4.7,
                true
        ));

        lista.add(new VeterinarioModel(
                "Pet Shop Bichinhos & Cia",
                "Rua Coronel Pedro Benedet, 755 - Centro, Criciúma - SC",
                "(48) 3437-2020",
                "Segunda à Sexta: 8h às 18h | Sábado: 8h às 16h",
                "Vacinação, Banho e Tosa, Produtos Veterinários",
                -28.6767, -49.3698,
                "Pet Shop",
                4.2,
                false
        ));

        lista.add(new VeterinarioModel(
                "Clínica Veterinária Amigo Fiel",
                "Rua Marechal Deodoro, 1200 - Centro, Criciúma - SC",
                "(48) 3434-7890",
                "Segunda à Sexta: 7h30 às 19h | Sábado: 8h às 14h",
                "Vacinação, Consultas, Exames Laboratoriais, Dermatologia",
                -28.6780, -49.3685,
                "Clínica Veterinária",
                4.4,
                false
        ));

        lista.add(new VeterinarioModel(
                "Hospital Veterinário PetCare",
                "Rua José Bonifácio, 890 - Próspera, Criciúma - SC",
                "(48) 3431-1234",
                "Segunda à Domingo: 7h às 22h",
                "Emergência, Cirurgias, Cardiologia, Oncologia",
                -28.6785, -49.3670,
                "Hospital Veterinário",
                4.6,
                true
        ));

        lista.add(new VeterinarioModel(
                "Pet Shop Mundo Animal",
                "Rua Anita Garibaldi, 567 - São Cristóvão, Criciúma - SC",
                "(48) 3435-4567",
                "Segunda à Sexta: 8h às 18h30 | Sábado: 8h às 17h",
                "Vacinação, Vermifugação, Banho e Tosa, Ração",
                -28.6790, -49.3660,
                "Pet Shop",
                4.1,
                false
        ));

        lista.add(new VeterinarioModel(
                "Clínica Veterinária Vida Animal",
                "Av. Universitária, 1789 - Universitário, Criciúma - SC",
                "(48) 3438-9876",
                "Segunda à Sexta: 8h às 19h | Sábado: 8h às 13h",
                "Vacinação, Consultas, Reprodução Animal, Nutrição",
                -28.6750, -49.3720,
                "Clínica Veterinária",
                4.3,
                false
        ));

        lista.add(new VeterinarioModel(
                "Hospital Veterinário 24h Emergency Pet",
                "Rua Henrique Lage, 2100 - Pinheirinho, Criciúma - SC",
                "(48) 3439-2424",
                "24 horas - Todos os dias",
                "Emergência 24h, UTI, Cirurgias Complexas, Radiologia",
                -28.6800, -49.3650,
                "Hospital Veterinário",
                4.8,
                true
        ));

        lista.add(new VeterinarioModel(
                "Pet Shop Cantinho dos Bichos",
                "Rua Barão do Rio Branco, 445 - Michel, Criciúma - SC",
                "(48) 3436-1111",
                "Segunda à Sexta: 8h às 18h | Sábado: 8h às 16h",
                "Vacinação, Produtos Pet, Banho e Tosa, Hospedagem",
                -28.6795, -49.3675,
                "Pet Shop",
                4.0,
                false
        ));

        lista.add(new VeterinarioModel(
                "Clínica Veterinária Saúde Animal",
                "Rua Domingos Ghizoni, 678 - São Luiz, Criciúma - SC",
                "(48) 3432-5678",
                "Segunda à Sexta: 7h às 18h | Sábado: 8h às 12h",
                "Vacinação, Consultas, Cirurgias, Fisioterapia Animal",
                -28.6760, -49.3710,
                "Clínica Veterinária",
                4.5,
                false
        ));

        return lista;
    }
}
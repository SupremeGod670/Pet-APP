package com.example.petapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;

public class CriarPetsActivity extends AppCompatActivity {

    private TextView voltar;
    public ImageView perfilpet;
    public Spinner editespecie, editsexo, editcidade, editraca, editestado;
    public EditText editnome, editbairro, editcel, editnascimento, edittel, editemail, editpai, editmae, editnaturalidade, editdescricao, editendereco;
    private Button salvarpet;
    public Button bcarregarimagem;
    public int PERMISION_CODE = 1001, IMAGE_CODE = 1000;
    private RegistroPetDAO registroPetDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_pets);

        // Initialize Views
        perfilpet = findViewById(R.id.perfilpet);

        editespecie = findViewById(R.id.editespecie);
        editraca = findViewById(R.id.editraca);
        editsexo = findViewById(R.id.editsexo);
        editestado = findViewById(R.id.editestado);
        editcidade = findViewById(R.id.editcidade);

        editnome = findViewById(R.id.editnome);
        editbairro = findViewById(R.id.editbairro);
        editnascimento = findViewById(R.id.editnascimento);
        editcel = findViewById(R.id.editcel);
        edittel = findViewById(R.id.edittel);
        editemail = findViewById(R.id.editemail);
        editpai = findViewById(R.id.editpai);
        editmae = findViewById(R.id.editmae);
        editnaturalidade = findViewById(R.id.editnaturalidade);
        editdescricao = findViewById(R.id.editdescricao);
        editendereco = findViewById(R.id.editendereco);

        bcarregarimagem = findViewById(R.id.bcarregarimagem);
        salvarpet = findViewById(R.id.salvarpet);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish current activity to prevent going back to it
            }
        });

        bcarregarimagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISION_CODE);
                } else {
                    escolherImagem();
                }
            }
        });

        editespecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = adapterView.getSelectedItemPosition();
                if (position > 0) {
                    String selectedItem = adapterView.getSelectedItem().toString();
                    switch (selectedItem) {
                        case "Cachorro":
                            editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.raca_cachorro, android.R.layout.simple_spinner_item));
                            break;
                        case "Gato":
                            editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.raca_gato, android.R.layout.simple_spinner_item));
                            break;
                        case "Pássaro":
                            editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.raca_ave, android.R.layout.simple_spinner_item));
                            break;
                        default:
                            editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.selecione_um_item, android.R.layout.simple_spinner_item));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.selecione_um_item, android.R.layout.simple_spinner_item));
            }
        });

        editestado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = adapterView.getSelectedItemPosition();
                if (position > 0) {
                    String selectedItem = adapterView.getSelectedItem().toString();
                    switch (selectedItem) {
                        case "AC":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ac, android.R.layout.simple_spinner_item));
                            break;
                        case "AL":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_al, android.R.layout.simple_spinner_item));
                            break;
                        case "AP":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ap, android.R.layout.simple_spinner_item));
                            break;
                        case "AM":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_am, android.R.layout.simple_spinner_item));
                            break;
                        case "BA":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ba, android.R.layout.simple_spinner_item));
                            break;
                        case "CE":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ce, android.R.layout.simple_spinner_item));
                            break;
                        case "DF":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_df, android.R.layout.simple_spinner_item));
                            break;
                        case "ES":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_es, android.R.layout.simple_spinner_item));
                            break;
                        case "GO":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_go, android.R.layout.simple_spinner_item));
                            break;
                        case "MA":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ma, android.R.layout.simple_spinner_item));
                            break;
                        case "MT":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_mt, android.R.layout.simple_spinner_item));
                            break;
                        case "MS":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ms, android.R.layout.simple_spinner_item));
                            break;
                        case "MG":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_mg, android.R.layout.simple_spinner_item));
                            break;
                        case "PA":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_pa, android.R.layout.simple_spinner_item));
                            break;
                        case "PB":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_pb, android.R.layout.simple_spinner_item));
                            break;
                        case "PR":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_pr, android.R.layout.simple_spinner_item));
                            break;
                        case "PE":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_pe, android.R.layout.simple_spinner_item));
                            break;
                        case "PI":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_pi, android.R.layout.simple_spinner_item));
                            break;
                        case "RJ":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_rj, android.R.layout.simple_spinner_item));
                            break;
                        case "RN":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_rn, android.R.layout.simple_spinner_item));
                            break;
                        case "RS":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_rs, android.R.layout.simple_spinner_item));
                            break;
                        case "RO":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_ro, android.R.layout.simple_spinner_item));
                            break;
                        case "RR":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_rr, android.R.layout.simple_spinner_item));
                            break;
                        case "SC":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_sc, android.R.layout.simple_spinner_item));
                            break;
                        case "SP":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_sp, android.R.layout.simple_spinner_item));
                            break;
                        case "SE":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_se, android.R.layout.simple_spinner_item));
                            break;
                        case "TO":
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.cidade_to, android.R.layout.simple_spinner_item));
                            break;
                        default:
                            editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.selecione_um_item, android.R.layout.simple_spinner_item));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editcidade.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.selecione_um_item, android.R.layout.simple_spinner_item));
            }
        });

        salvarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText and Spinner, with null/empty checks
                String nome = editnome.getText().toString().trim();
                String nascimentoStr = editnascimento.getText().toString().trim();
                String especie = editespecie.getSelectedItem() != null ? editespecie.getSelectedItem().toString() : "";
                String raca = editraca.getSelectedItem() != null ? editraca.getSelectedItem().toString() : "";
                String sexo = editsexo.getSelectedItem() != null ? editsexo.getSelectedItem().toString() : "";
                String cidade = editcidade.getSelectedItem() != null ? editcidade.getSelectedItem().toString() : "";
                String estado = editestado.getSelectedItem() != null ? editestado.getSelectedItem().toString() : "";
                String bairro = editbairro.getText().toString().trim();
                String telStr = edittel.getText().toString().trim();
                String celStr = editcel.getText().toString().trim();
                String email = editemail.getText().toString().trim();
                String pai = editpai.getText().toString().trim();
                String mae = editmae.getText().toString().trim();
                String naturalidade = editnaturalidade.getText().toString().trim();
                String descricao = editdescricao.getText().toString().trim();
                String endereco = editendereco.getText().toString().trim();

                // Log retrieved values for debugging
                Log.d("CriarPetsActivity", "Nome: " + nome);
                Log.d("CriarPetsActivity", "Nascimento String: " + nascimentoStr);
                Log.d("CriarPetsActivity", "Especie: " + especie);
                Log.d("CriarPetsActivity", "Raça: " + raca);
                Log.d("CriarPetsActivity", "Sexo: " + sexo);
                Log.d("CriarPetsActivity", "Cidade: " + cidade);
                Log.d("CriarPetsActivity", "Estado: " + estado);
                Log.d("CriarPetsActivity", "Bairro: " + bairro);
                Log.d("CriarPetsActivity", "Tel String: " + telStr);
                Log.d("CriarPetsActivity", "Cel String: " + celStr);
                Log.d("CriarPetsActivity", "Email: " + email);
                Log.d("CriarPetsActivity", "Pai: " + pai);
                Log.d("CriarPetsActivity", "Mae: " + mae);
                Log.d("CriarPetsActivity", "Naturalidade: " + naturalidade);
                Log.d("CriarPetsActivity", "Descrição: " + descricao);
                Log.d("CriarPetsActivity", "Endereço: " + endereco);

                // Input validation
                if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty() || sexo.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
                    Toast.makeText(CriarPetsActivity.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
                    Toast.makeText(CriarPetsActivity.this, "(Nome, Espécie, Raça, Sexo, Cidade, Estado).", Toast.LENGTH_LONG).show();
                    return; // Stop execution
                }

                Double nascimento = null;
                if (!nascimentoStr.isEmpty()) {
                    try {
                        nascimento = Double.valueOf(nascimentoStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Nascimento para Double: " + nascimentoStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de nascimento inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return; // Stop execution
                    }
                }

                Double tel = null;
                if (!telStr.isEmpty()) {
                    try {
                        tel = Double.valueOf(telStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Telefone Residencial para Double: " + telStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de telefone residencial inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return; // Stop execution
                    }
                }

                Double cel = null;
                if (!celStr.isEmpty()) {
                    try {
                        cel = Double.valueOf(celStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Telefone Celular para Double: " + celStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de telefone celular inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return; // Stop execution
                    }
                }

                registroPetDAO = new RegistroPetDAO(CriarPetsActivity.this);

                RegistroPetModel pet = new RegistroPetModel();
                pet.setNomepet(nome);
                pet.setNascimento(nascimento); // nascimento can be null
                pet.setEspecie(especie);
                pet.setRaca(raca);
                pet.setSexo(sexo);
                pet.setCidade(cidade);
                pet.setEstado(estado);
                pet.setBairro(bairro);
                pet.setTelefoneresd(tel); // tel can be null
                pet.setTelefonecel(cel); // cel can be null
                pet.setEmail(email);
                pet.setPai(pai);
                pet.setMae(mae);
                pet.setNaturalidade(naturalidade);
                pet.setDescricao(descricao);
                pet.setEndereco(endereco);

                registroPetDAO.insert(pet);

                Toast.makeText(CriarPetsActivity.this, "Pet registrado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CriarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void escolherImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Call super method

        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE && data != null && data.getData() != null) {
            perfilpet.setImageURI(data.getData());
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Seleção de imagem cancelada.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method

        if (requestCode == PERMISION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                escolherImagem();
            } else {
                Toast.makeText(this, "Permissão para acessar o armazenamento negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
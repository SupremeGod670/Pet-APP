package com.example.petapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapp.R;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CriarPetsActivity extends AppCompatActivity {

    private TextView voltar;
    public ImageView perfilpet;
    public Spinner editespecie, editsexo, editcidade, editraca, editestado;
    public EditText editnome, editbairro, editcep, editcel, editnascimento, edittel, editemail, editpai, editmae, editnaturalidade, editdescricao, editendereco, editcor;
    private Button salvarpet;
    public Button bcarregarimagem;
    public int PERMISION_CODE = 1001, IMAGE_CODE = 1000;
    private RequestQueue requestQueue;
    private boolean cepPreencheuLocalizacao = false;
    private boolean isUpdatingSpinnersProgrammatically = false; // Flag para evitar loops nos listeners dos spinners
    private RegistroPetDAO registroPetDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_pets);

        requestQueue = Volley.newRequestQueue(this);

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
        editcep = findViewById(R.id.editcep);
        editnaturalidade = findViewById(R.id.editnaturalidade);
        editdescricao = findViewById(R.id.editdescricao);
        editendereco = findViewById(R.id.editendereco);
        editcor = findViewById(R.id.editcor);

        salvarpet = findViewById(R.id.salvarpet);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        perfilpet.setOnClickListener(new View.OnClickListener() {
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

        editcep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cep = s.toString().replaceAll("[^0-9]", ""); // Remove não numéricos
                if (cep.length() == 8) { // Verifica se o CEP tem 8 dígitos
                    buscarCep(cep);
                } else {
                    // Limpa os campos se o CEP não estiver completo ou for inválido
                    limparCamposEndereco();
                }
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

        formatFields();

        salvarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editnome.getText().toString().trim();
                String nascimentoStr = editnascimento.getText().toString().trim();
                String especie = editespecie.getSelectedItem() != null ? editespecie.getSelectedItem().toString() : "";
                String raca = editraca.getSelectedItem() != null ? editraca.getSelectedItem().toString() : "";
                String sexo = editsexo.getSelectedItem() != null ? editsexo.getSelectedItem().toString() : "";
                String cidade = editcidade.getSelectedItem() != null ? editcidade.getSelectedItem().toString() : "";
                String estado = editestado.getSelectedItem() != null ? editestado.getSelectedItem().toString() : "";
                String cepStr = editcep.getText().toString().trim();
                String bairro = editbairro.getText().toString().trim();
                String telStr = edittel.getText().toString().trim();
                String celStr = editcel.getText().toString().trim();
                String email = editemail.getText().toString().trim();
                String pai = editpai.getText().toString().trim();
                String mae = editmae.getText().toString().trim();
                String naturalidade = editnaturalidade.getText().toString().trim();
                String cor = editcor.getText().toString().trim();
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
                Log.d("CriarPetsActivity", "CEP: " + cepStr);
                Log.d("CriarPetsActivity", "Tel String: " + telStr);
                Log.d("CriarPetsActivity", "Cel String: " + celStr);
                Log.d("CriarPetsActivity", "Email: " + email);
                Log.d("CriarPetsActivity", "Pai: " + pai);
                Log.d("CriarPetsActivity", "Mae: " + mae);
                Log.d("CriarPetsActivity", "Naturalidade: " + naturalidade);
                Log.d("CriarPetsActivity", "Cor: " + cor);
                Log.d("CriarPetsActivity", "Descrição: " + descricao);
                Log.d("CriarPetsActivity", "Endereço: " + endereco);

                if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty() || sexo.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
                    Toast.makeText(CriarPetsActivity.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
                    Toast.makeText(CriarPetsActivity.this, "(Nome, Espécie, Raça, Estado, Cidade).", Toast.LENGTH_LONG).show();
                    return;
                }

                Double nascimento = null;
                if (!nascimentoStr.isEmpty()) {
                    try {
                        nascimento = Double.valueOf(nascimentoStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Nascimento para Double: " + nascimentoStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de nascimento inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Double cep = null;
                if (!cepStr.isEmpty()) {
                    try {
                        cep = Double.valueOf(cepStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter CEP para Double: " + cepStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de CEP inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                    }
                }

                Double tel = null;
                if (!telStr.isEmpty()) {
                    try {
                        tel = Double.valueOf(telStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Telefone Residencial para Double: " + telStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de telefone residencial inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Double cel = null;
                if (!celStr.isEmpty()) {
                    try {
                        cel = Double.valueOf(celStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Telefone Celular para Double: " + celStr, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de telefone celular inválido. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                registroPetDAO = new RegistroPetDAO(CriarPetsActivity.this);

                RegistroPetModel pet = new RegistroPetModel();
                pet.setNomepet(nome);
                pet.setNascimento(nascimento);
                pet.setEspecie(especie);
                pet.setRaca(raca);
                pet.setSexo(sexo);
                pet.setCidade(cidade);
                pet.setEstado(estado);
                pet.setBairro(bairro);
                pet.setCep(cep);
                pet.setTelefoneresd(tel);
                pet.setTelefonecel(cel);
                pet.setEmail(email);
                pet.setPai(pai);
                pet.setMae(mae);
                pet.setNaturalidade(naturalidade);
                pet.setDescricao(descricao);
                pet.setEndereco(endereco);

                registroPetDAO.insert(pet);

                if (registroPetDAO.selectNotNull(nome, especie, raca, estado, cidade)) {
                    Toast.makeText(CriarPetsActivity.this, "Pet registrado!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (registroPetDAO.select(nome, nascimentoStr, especie, sexo, pai, mae, raca, naturalidade, cor, endereco, bairro, cidade, telStr, email, cepStr, estado, celStr, descricao)) {
                    Toast.makeText(CriarPetsActivity.this, "Pet registrado, e com todos os campos preenchidos!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CriarPetsActivity.this, "Erro ao registrar pet.", Toast.LENGTH_SHORT).show();
                }

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
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE && data != null && data.getData() != null) {
            perfilpet.setImageURI(data.getData());
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Seleção de imagem cancelada.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                escolherImagem();
            } else {
                Toast.makeText(this, "Permissão para acessar o armazenamento negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buscarCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("erro")) {
                                Toast.makeText(CriarPetsActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                                limparCamposEndereco();
                                return;
                            }

                            String logradouro = response.getString("logradouro");
                            String bairro = response.getString("bairro");
                            String cidade = response.getString("localidade");
                            String estado = response.getString("uf");

                            editendereco.setText(logradouro);
                            editbairro.setText(bairro);

                            // Selecionar o estado no Spinner
                            selecionarItemSpinner(editestado, estado);

                            // Para o Spinner de cidade, a lógica pode ser mais complexa
                            // se você carrega as cidades dinamicamente baseado no estado.
                            // A API já retorna a cidade correta. Você pode:
                            // 1. Tentar selecionar a cidade diretamente se ela já existir no adapter.
                            // 2. Ou, se você preenche o spinner de cidades baseado no estado,
                            //    após selecionar o estado, o adapter de cidades será atualizado,
                            //    e então você pode tentar selecionar a cidade.
                            // Por simplicidade, este exemplo tentará selecionar a cidade.
                            // Se o seu spinner de cidades é populado dinamicamente APÓS a seleção do estado,
                            // você precisará ajustar esta parte.

                            // Forçar a atualização do adapter de cidades se necessário
                            // (Isso depende de como seu editestado.setOnItemSelectedListener está configurado)
                            if (editestado.getSelectedItemPosition() > 0) {
                                // Disparar o listener do estado para carregar as cidades corretas se necessário
                                // Ou carregar o adapter de cidades diretamente aqui
                                // Exemplo: (Supondo que você tenha um método para carregar cidades por estado)
                                // carregarCidadesPorEstado(estado);
                            }
                            selecionarItemSpinner(editcidade, cidade);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CriarPetsActivity.this, "Erro ao processar dados do CEP", Toast.LENGTH_SHORT).show();
                            limparCamposEndereco();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CEP_API_ERROR", "Erro: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("CEP_API_ERROR", "Status Code: " + error.networkResponse.statusCode);
                        }
                        Toast.makeText(CriarPetsActivity.this, "Erro ao buscar CEP. Verifique sua conexão.", Toast.LENGTH_LONG).show();
                        limparCamposEndereco();
                    }
                });

        // Adiciona a requisição à fila
        requestQueue.add(jsonObjectRequest);
    }

    private void selecionarItemSpinner(Spinner spinner, String valor) {
        if (valor == null || valor.isEmpty()) return;

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(valor)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void limparCamposEndereco() {
        editendereco.setText("");
        editbairro.setText("");
        // Você pode resetar os spinners de estado e cidade para o item "Selecione"
        if (editestado.getAdapter() != null && editestado.getAdapter().getCount() > 0) {
            editestado.setSelection(0); // Supondo que o primeiro item é "Selecione"
        }
        if (editcidade.getAdapter() != null && editcidade.getAdapter().getCount() > 0) {
            // O spinner de cidade pode precisar ser limpo ou resetado para "Selecione"
            // Se ele depende do estado, e o estado foi resetado, ele também deveria ser.
            ArrayAdapter<CharSequence> adapterCidadePadrao = ArrayAdapter.createFromResource(this, R.array.selecione_um_item, android.R.layout.simple_spinner_item);
            editcidade.setAdapter(adapterCidadePadrao);
            editcidade.setSelection(0);
        }
    }

    private void formatFields() {
        editcep.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                old = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String str = s.toString().replaceAll("[^\\d]", "");
                String mascara = "#####-###";

                String novaString = "";
                if (str.length() > old.replaceAll("[^\\d]", "").length()) { // Digitado
                    int i = 0;
                    for (char m : mascara.toCharArray()) {
                        if (m != '#' && str.length() > old.replaceAll("[^\\d]", "").length()) {
                            novaString += m;
                            continue;
                        }
                        try {
                            novaString += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                } else { // Apagado
                    novaString = str;
                }

                isUpdating = true;
                editcep.setText(novaString);
                editcep.setSelection(novaString.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editnascimento.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                old = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String str = s.toString().replaceAll("[^\\d]", "");
                String mascara = "##/##/####";

                String novaString = "";
                if (str.length() > old.replaceAll("[^\\d]", "").length()) { // Digitado
                    int i = 0;
                    for (char m : mascara.toCharArray()) {
                        if (m != '#' && str.length() > old.replaceAll("[^\\d]", "").length()) {
                            novaString += m;
                            continue;
                        }
                        try {
                            novaString += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                } else { // Apagado
                    novaString = str;
                }

                isUpdating = true;
                editnascimento.setText(novaString);
                editnascimento.setSelection(novaString.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Adicionar TextWatchers para os campos de telefone (edittel e editcel)
        // Exemplo para edittel (telefone fixo)
        addPhoneNumberMask(edittel, "(##) ####-####");
        // Exemplo para editcel (telefone celular)
        addPhoneNumberMask(editcel, "(##) #####-####");
    }

    private void addPhoneNumberMask(final EditText editText, final String mask) {
        editText.addTextChangedListener(new MaskedWatcher(mask, editText));
    }

    private static class MaskedWatcher implements TextWatcher {
        private final String mask;
        private final EditText editText;
        private boolean isUpdating;
        private String old = "";

        MaskedWatcher(String mask, EditText editText) {
            this.mask = mask;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            old = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isUpdating) {
                isUpdating = false;
                return;
            }

            String str = s.toString().replaceAll("[^\\d]", "");
            String formatted = "";
            int i = 0;
            for (char m : mask.toCharArray()) {
                if (m != '#') {
                    formatted += m;
                    continue;
                }
                try {
                    formatted += str.charAt(i);
                } catch (Exception e) {
                    break;
                }
                i++;
            }

            isUpdating = true;
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
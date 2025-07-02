package com.example.petapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapp.R;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CriarPetsActivity extends AppCompatActivity {

    private TextView voltar;
    public ImageView perfilpet;
    public Spinner editespecie, editsexo, editcidade, editraca, editestado;
    public EditText editnome, editbairro, editcep, editcel, editnascimento, edittel, editemail, editpai, editmae, editnaturalidade, editdescricao, editendereco, editcor;
    private Button salvarpet;
    public int PERMISION_CODE = 1001, IMAGE_CODE = 1000;
    private RequestQueue requestQueue;
    private RegistroPetDAO registroPetDAO;

    private String imagemPerfilUrl = null;
    private String cidadeSelecionadaPorCep = null;


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

        ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(this,
                R.array.estado, android.R.layout.simple_spinner_item);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editestado.setAdapter(estadoAdapter);


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
                } else {
                    editraca.setAdapter(ArrayAdapter.createFromResource(CriarPetsActivity.this, R.array.selecione_um_item, android.R.layout.simple_spinner_item));
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
                String cep = s.toString().replaceAll("[^0-9]", "");
                if (cep.length() == 8) {
                    buscarCep(cep);
                } else {
                    limparCamposEndereco();
                }
            }
        });

        editestado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    String ufSelecionado = adapterView.getItemAtPosition(position).toString();
                    if (!ufSelecionado.equals("Selecione um Estado")) {
                        buscarCidadesPorEstado(ufSelecionado);
                    } else {
                        ArrayAdapter<String> adapterCidadePadrao = new ArrayAdapter<>(CriarPetsActivity.this,
                                android.R.layout.simple_spinner_item, new String[]{"Selecione uma Cidade"});
                        adapterCidadePadrao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        editcidade.setAdapter(adapterCidadePadrao);
                    }
                } else {
                    ArrayAdapter<String> adapterCidadePadrao = new ArrayAdapter<>(CriarPetsActivity.this,
                            android.R.layout.simple_spinner_item, new String[]{"Selecione uma Cidade"});
                    adapterCidadePadrao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    editcidade.setAdapter(adapterCidadePadrao);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ArrayAdapter<String> adapterCidadePadrao = new ArrayAdapter<>(CriarPetsActivity.this,
                        android.R.layout.simple_spinner_item, new String[]{"Selecione uma Cidade"});
                adapterCidadePadrao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editcidade.setAdapter(adapterCidadePadrao);
            }
        });


        formatFields();

        salvarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editnome.getText().toString().trim();
                String nascimentoStr = editnascimento.getText().toString().trim();
                String especie = editespecie.getSelectedItem() != null && editespecie.getSelectedItemPosition() > 0 ? editespecie.getSelectedItem().toString() : "";
                String raca = editraca.getSelectedItem() != null && editraca.getSelectedItemPosition() > 0 ? editraca.getSelectedItem().toString() : "";
                String sexo = editsexo.getSelectedItem() != null && editsexo.getSelectedItemPosition() > 0 ? editsexo.getSelectedItem().toString() : "";
                String cidade = editcidade.getSelectedItem() != null && editcidade.getSelectedItemPosition() > 0 ? editcidade.getSelectedItem().toString() : "";
                String estado = editestado.getSelectedItem() != null && editestado.getSelectedItemPosition() > 0 ? editestado.getSelectedItem().toString() : "";
                String cepStr = editcep.getText().toString().trim().replaceAll("[^0-9]", "");
                String bairro = editbairro.getText().toString().trim();
                String telStr = edittel.getText().toString().trim().replaceAll("[^0-9]", "");
                String celStr = editcel.getText().toString().trim().replaceAll("[^0-9]", "");
                String email = editemail.getText().toString().trim();
                String pai = editpai.getText().toString().trim();
                String mae = editmae.getText().toString().trim();
                String naturalidade = editnaturalidade.getText().toString().trim();
                String cor = editcor.getText().toString().trim();
                String descricao = editdescricao.getText().toString().trim();
                String endereco = editendereco.getText().toString().trim();

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


                if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty() || sexo.isEmpty() || cidade.isEmpty() || estado.isEmpty() || cidade.equals("Selecione uma Cidade") || estado.equals("Selecione um Estado")) {
                    Toast.makeText(CriarPetsActivity.this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
                    Toast.makeText(CriarPetsActivity.this, "(Nome, Espécie, Raça, Sexo, Estado, Cidade).", Toast.LENGTH_LONG).show();
                    return;
                }

                Double nascimento = null;

                String nascimentoDigitsOnly = nascimentoStr.replaceAll("[^\\d]", "");
                if (!nascimentoDigitsOnly.isEmpty() && nascimentoDigitsOnly.length() == 8) {
                    try {
                        nascimento = Double.valueOf(nascimentoDigitsOnly);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Nascimento para Double: " + nascimentoDigitsOnly, e);
                        Toast.makeText(CriarPetsActivity.this, "Formato de nascimento inválido.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                Double cepDouble = null;
                if (!cepStr.isEmpty()) {
                    try {
                        cepDouble = Double.valueOf(cepStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter CEP para Double: " + cepStr, e);
                    }
                }

                Double telDouble = null;
                if (!telStr.isEmpty()) {
                    try {
                        telDouble = Double.valueOf(telStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Telefone para Double: " + telStr, e);
                    }
                }

                Double celDouble = null;
                if (!celStr.isEmpty()) {
                    try {
                        celDouble = Double.valueOf(celStr);
                    } catch (NumberFormatException e) {
                        Log.e("CriarPetsActivity", "Erro ao converter Celular para Double: " + celStr, e);
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
                pet.setCep(cepDouble);
                pet.setTelefoneresd(telDouble);
                pet.setTelefonecel(celDouble);
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
            Uri imageUri = data.getData();
            perfilpet.setImageURI(imageUri);
            imagemPerfilUrl = imageUri.toString();

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
        cidadeSelecionadaPorCep = null;

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

                            String logradouro = response.optString("logradouro");
                            String bairro = response.optString("bairro");
                            String cidade = response.optString("localidade");
                            String estadoUF = response.optString("uf");

                            editendereco.setText(logradouro);
                            editbairro.setText(bairro);

                            selecionarItemSpinner(editestado, estadoUF);

                            cidadeSelecionadaPorCep = cidade;

                        } catch (Exception e) {
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
        requestQueue.add(jsonObjectRequest);
    }

    private void buscarCidadesPorEstado(String uf) {
        if (uf == null || uf.isEmpty() || uf.equals("Selecione um Estado")) {
            ArrayAdapter<String> adapterCidadePadrao = new ArrayAdapter<>(CriarPetsActivity.this,
                    android.R.layout.simple_spinner_item, new String[]{"Selecione uma Cidade"});
            adapterCidadePadrao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editcidade.setAdapter(adapterCidadePadrao);
            return;
        }

        String urlCidades = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + uf + "/municipios";
        Log.d("IBGE_API", "Fetching cities for UF: " + uf + " from URL: " + urlCidades);


        List<String> cidadesDefault = new ArrayList<>();
        cidadesDefault.add("Carregando cidades...");
        ArrayAdapter<String> loadingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cidadesDefault);
        loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editcidade.setAdapter(loadingAdapter);
        editcidade.setEnabled(false);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlCidades, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> cidades = new ArrayList<>();
                        cidades.add("Selecione uma Cidade");
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject municipio = response.getJSONObject(i);
                                cidades.add(municipio.getString("nome"));
                            }
                            ArrayAdapter<String> adapterCidades = new ArrayAdapter<>(CriarPetsActivity.this,
                                    android.R.layout.simple_spinner_item, cidades);
                            adapterCidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            editcidade.setAdapter(adapterCidades);
                            editcidade.setEnabled(true);

                            if (cidadeSelecionadaPorCep != null) {
                                selecionarItemSpinner(editcidade, cidadeSelecionadaPorCep);
                                cidadeSelecionadaPorCep = null;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CriarPetsActivity.this, "Erro ao processar lista de cidades", Toast.LENGTH_SHORT).show();
                            resetCitySpinnerToDefault();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("IBGE_API_ERROR", "Erro ao buscar cidades: " + error.toString());
                if (error.networkResponse != null) {
                    Log.e("IBGE_API_ERROR", "Status Code: " + error.networkResponse.statusCode);
                }
                Toast.makeText(CriarPetsActivity.this, "Erro ao buscar cidades. Verifique a conexão ou UF.", Toast.LENGTH_LONG).show();
                resetCitySpinnerToDefault();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void resetCitySpinnerToDefault() {
        List<String> cidadesDefault = new ArrayList<>();
        cidadesDefault.add("Falha ao carregar cidades");
        ArrayAdapter<String> errorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cidadesDefault);
        errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editcidade.setAdapter(errorAdapter);
        editcidade.setEnabled(false);
    }


    private void selecionarItemSpinner(Spinner spinner, String valor) {
        if (valor == null || valor.isEmpty() || spinner.getAdapter() == null) {
            return;
        }
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(valor)) {
                spinner.setSelection(i);
                break;
            }
        }
    }


    private void limparCamposEndereco() {
        editendereco.setText("");
        editbairro.setText("");
        if (editestado.getAdapter() != null && editestado.getAdapter().getCount() > 0) {
            editestado.setSelection(0);
        }
        ArrayAdapter<String> adapterCidadePadrao = new ArrayAdapter<>(CriarPetsActivity.this,
                android.R.layout.simple_spinner_item, new String[]{"Selecione uma Cidade"});
        adapterCidadePadrao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editcidade.setAdapter(adapterCidadePadrao);
    }

    private void formatFields() {
        editcep.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";
            private final String MASK_CEP = "#####-###";


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
                String mascara = MASK_CEP;

                String novaString = "";
                int i = 0;
                for (char m : mascara.toCharArray()) {
                    if (i >= str.length()) break;

                    if (m != '#') {
                        novaString += m;
                    } else {
                        novaString += str.charAt(i);
                        i++;
                    }
                }
                isUpdating = true;
                editcep.setText(novaString);
                editcep.setSelection(novaString.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cepDigits = s.toString().replaceAll("[^0-9]", "");
                if (cepDigits.length() == 8) {
                    buscarCep(cepDigits);
                } else if (s.length() < MASK_CEP.length() && s.length() < old.length()) {
                    limparCamposEndereco();
                }
            }
        });

        editnascimento.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String old = "";
            private final String MASK_NASCIMENTO = "##/##/####";


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
                String mascara = MASK_NASCIMENTO;
                String novaString = "";
                int i = 0;

                for (char m : mascara.toCharArray()) {
                    if (i >= str.length()) break;

                    if (m != '#') {
                        novaString += m;
                    } else {
                        novaString += str.charAt(i);
                        i++;
                    }
                }
                isUpdating = true;
                editnascimento.setText(novaString);
                editnascimento.setSelection(novaString.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        addPhoneNumberMask(edittel, "(##) ####-####");
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
            int maskCharIndex = 0;


            for (maskCharIndex = 0; maskCharIndex < mask.length() && i < str.length(); maskCharIndex++) {
                char m = mask.charAt(maskCharIndex);
                if (m != '#') {
                    formatted += m;
                } else {
                    formatted += str.charAt(i);
                    i++;
                }
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
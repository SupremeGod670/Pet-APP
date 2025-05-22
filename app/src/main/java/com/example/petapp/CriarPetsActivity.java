package com.example.petapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
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
    private Spinner editespecie, editsexo, editcidade, editraca, editestado;
    private EditText editnome, editbairro, editcel, editnascimento, edittel, editemail, editpai, editmae, editnaturalidade, editdescricao, editendereco;
    private Button salvarpet;
    public Button bcarregarimagem;
    public int PERMISION_CODE = 1001, IMAGE_CODE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_pets);

        perfilpet = findViewById(R.id.perfilpet);

        editespecie = findViewById(R.id.editespecie);
        editsexo = findViewById(R.id.editsexo);
        editcidade = findViewById(R.id.editcidade);
        editraca = findViewById(R.id.editraca);
        editestado = findViewById(R.id.editestado);

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


        salvarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editnome.getText().toString();
                String nascimento = editnascimento.getText().toString();
                String especie = editespecie.getSelectedItem().toString();
                String sexo = editsexo.getSelectedItem().toString();
                String cidade = editcidade.getSelectedItem().toString();
                String estado = editestado.getSelectedItem().toString();
                String bairro = editbairro.getText().toString();
                String tel = edittel.getText().toString();
                String cel = editcel.getText().toString();
                String email = editemail.getText().toString();
                String pai = editpai.getText().toString();
                String mae = editmae.getText().toString();
                String naturalidade = editnaturalidade.getText().toString();
                String descricao = editdescricao.getText().toString();
                String endereco = editendereco.getText().toString();

                RegistroPetDAO dao = new RegistroPetDAO(CriarPetsActivity.this);

                RegistroPetModel registroPetModel = new RegistroPetModel();
                registroPetModel.setNomepet(nome);
                registroPetModel.setNascimento(Double.valueOf(nascimento));
                registroPetModel.setEspecie(especie);
                registroPetModel.setSexo(sexo);
                registroPetModel.setCidade(cidade);
                registroPetModel.setEstado(estado);
                registroPetModel.setBairro(bairro);
                registroPetModel.setTelefoneresd(Double.valueOf(tel));
                registroPetModel.setTelefonecel(Double.valueOf(cel));
                registroPetModel.setEmail(email);
                registroPetModel.setPai(pai);
                registroPetModel.setMae(mae);
                registroPetModel.setNaturalidade(naturalidade);
                registroPetModel.setDescricao(descricao);
                registroPetModel.setEndereco(endereco);

                dao.insert(registroPetModel);

                Intent intent = new Intent(CriarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
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

        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE) {
                perfilpet.setImageURI(data.getData());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {

        switch (requestCode) {

            case 1001: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    escolherImagem();
                } else {
                    Toast.makeText(this, "Permissão Negada", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
}

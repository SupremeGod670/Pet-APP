package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;

public class EditarPetsActivity extends AppCompatActivity {

    private TextView voltar;
    private ImageView perfilpet;
    private Spinner editespecie, editsexo, editcidade, editraca, editestado;
    private EditText editnome, editbairro, editnascimento, edittel, editemail, editpai, editmae, editnaturalidade, editdescricao, editendereco;
    private Button alterarpet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_pets);

        perfilpet = findViewById(R.id.perfilpet);

        editespecie = findViewById(R.id.editespecie);
        editsexo = findViewById(R.id.editsexo);
        editcidade = findViewById(R.id.editcidade);
        editraca = findViewById(R.id.editraca);
        editestado = findViewById(R.id.editestado);

        editnome = findViewById(R.id.editnome);
        editbairro = findViewById(R.id.editbairro);
        editnascimento = findViewById(R.id.editnascimento);
        edittel = findViewById(R.id.edittel);
        editemail = findViewById(R.id.editemail);
        editpai = findViewById(R.id.editpai);
        editmae = findViewById(R.id.editmae);
        editnaturalidade = findViewById(R.id.editnaturalidade);
        editdescricao = findViewById(R.id.editdescricao);
        editendereco = findViewById(R.id.editendereco);

        alterarpet = findViewById(R.id.alterarpet);

        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        alterarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPetsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

    }
}

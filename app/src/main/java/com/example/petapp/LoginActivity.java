package com.example.petapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.database.model.RegistroUserModel;

public class LoginActivity extends AppCompatActivity {

    private ImageView pets;
    private EditText email;
    private EditText senha;
    private Button acessar;
    private TextView criar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pets = findViewById(R.id.pets);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        acessar = findViewById(R.id.acessar);
        criar = findViewById(R.id.criar);

        RegistroUserModel user = new RegistroUserModel();

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, CriarActivity.class);
                startActivity(it);
            }
        });

        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    //Colocar mensagem de erro
                    mensagemErrorApresentar("Campo email obrigatório");
                    return;
                }

                if (senha.getText().toString().isEmpty()) {
                    //Colocar mensagem de erro
                    mensagemErrorApresentar("Campo senha obrigatório");
                    return;
                } else if (email.getText().toString() == user.getEmail() && senha.getText().toString() == user.getSenha()) {
                    Intent it = new Intent(LoginActivity.this, MenuPetsActivity.class);
                    startActivity(it);
                } else {
                    mensagemErrorApresentar("Email ou senha incorretos");
                }
            }
        });

    }

    private void mensagemErrorApresentar(String mensagem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(mensagem);
        builder.setTitle("Informação");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

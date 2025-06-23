package com.example.petapp.activities;

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

import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;

public class LoginActivity extends AppCompatActivity {

    private ImageView pets;
    private EditText email;
    private EditText senha;
    private Button acessar;
    private TextView criar;
    private RegistroUserDAO registroUserDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pets = findViewById(R.id.pets);
        this.email = findViewById(R.id.email);
        this.senha = findViewById(R.id.senha);
        acessar = findViewById(R.id.acessar);
        criar = findViewById(R.id.crie_conta);

        registroUserDAO = new RegistroUserDAO(LoginActivity.this);

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
                String emailDigitado = email.getText().toString();
                String senhaDigitada = senha.getText().toString();

                if (emailDigitado.isEmpty()) {
                    mensagemErrorApresentar("Campo email obrigatório");
                    return;
                }

                if (senhaDigitada.isEmpty()) {
                    mensagemErrorApresentar("Campo senha obrigatório");
                    return;
                }

                if (registroUserDAO.select(emailDigitado, senhaDigitada)) {
                    Intent it = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(it);
                } else if (!registroUserDAO.selectEmail(emailDigitado) && !registroUserDAO.selectSenha(senhaDigitada)) {
                    mensagemErrorApresentar("Email e senha incorretos");
                } else if (!registroUserDAO.selectEmail(emailDigitado)){
                    mensagemErrorApresentar("Email incorreto");
                } else if (!registroUserDAO.selectSenha(senhaDigitada)){
                    mensagemErrorApresentar("Senha incorreta");
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
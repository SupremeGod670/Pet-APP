package com.example.petapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriarActivity extends AppCompatActivity {

    private TextView voltar;
    private ImageView pets;
    private EditText email, senha;
    private Button criar;
    private TextView minimo, caracter_especial, letra_maiscula, letra_minuscula;

    private RegistroUserDAO registroUserDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_conta);

        voltar = findViewById(R.id.voltar);

        pets = findViewById(R.id.pets);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        criar = findViewById(R.id.criar_conta);
        minimo = findViewById(R.id.minimo);
        caracter_especial = findViewById(R.id.caracter_especial);
        letra_maiscula = findViewById(R.id.letra_maiscula);
        letra_minuscula = findViewById(R.id.letra_minuscula);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CriarActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });

        senha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (isPasswordValid(password)) {
                    minimo.setText("⩗ Minimo de 6 caracteres");
                    caracter_especial.setText("⩗ Minimo de um caracter especial");
                    letra_maiscula.setText("⩗ Minimo de uma letra maiuscula");
                    letra_minuscula.setText("⩗ Minimo de uma letra minuscula");
                    minimo.setTextColor(Color.GREEN);
                    caracter_especial.setTextColor(Color.GREEN);
                    letra_maiscula.setTextColor(Color.GREEN);
                    letra_minuscula.setTextColor(Color.GREEN);

                } else {
                    minimo.setTextColor(password.length() > 6 ? Color.GREEN : Color.RED);
                    caracter_especial.setTextColor(containsSpecialChar(password) ? Color.GREEN : Color.RED);
                    letra_maiscula.setTextColor(containsUpperCase(password) ? Color.GREEN : Color.RED);
                    letra_minuscula.setTextColor(containsLowerCase(password) ? Color.GREEN : Color.RED);
                }
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String password = senha.getText().toString();

                if (email.getText().toString().isEmpty()) {
                    //Colocar mensagem de erro
                    mensagemErrorApresentar("Campo email obrigatório");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    mensagemErrorApresentar("Email inválido");
                    return;
                }

                if (senha.getText().toString().isEmpty()) {
                    //Colocar mensagem de erro
                    mensagemErrorApresentar("Campo senha obrigatório");
                    return;
                }

                if (!isPasswordValid(password)) {
                    mensagemErrorApresentar("Senha inválida. Verifique os requisitos.");
                    return;
                } else {
                    registroUserDAO = new RegistroUserDAO(CriarActivity.this);

                    RegistroUserModel user = new RegistroUserModel();
                    user.setEmail(emailStr);
                    user.setSenha(password);

                    registroUserDAO.insert(user);

                    Intent it = new Intent(CriarActivity.this, LoginActivity.class);
                    startActivity(it);
                }
            }
        });

    }

    private boolean isPasswordValid(String password) {
        if (password.length() <= 6) {
            return false;
        }
        if (!containsSpecialChar(password)) {
            return false;
        }
        if (!containsUpperCase(password)) {
            return false;
        }
        if (!containsLowerCase(password)) {
            return false;
        }
        return true;
    }

    private boolean containsSpecialChar(String password) {
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        return specialCharMatcher.find();
    }

    private boolean containsUpperCase(String password) {
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        return upperCaseMatcher.find();
    }

    private boolean containsLowerCase(String password) {
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        return lowerCaseMatcher.find();
    }

    private void mensagemErrorApresentar(String mensagem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CriarActivity.this);
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

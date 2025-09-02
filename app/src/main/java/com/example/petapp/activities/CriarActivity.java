package com.example.petapp.activities;

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

import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;
import com.example.petapp.utils.HashUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriarActivity extends AppCompatActivity {

    private TextView voltar;
    private ImageView pets;
    private EditText nome, email, senha;
    private Button criar;
    private TextView minimo, caracter_especial, letra_maiscula, letra_minuscula;

    private RegistroUserDAO registroUserDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_conta);

        inicializarComponentes();
        configurarListeners();
    }

    private void inicializarComponentes() {
        voltar = findViewById(R.id.voltar);
        pets = findViewById(R.id.pets);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        criar = findViewById(R.id.criar_conta);
        minimo = findViewById(R.id.minimo);
        caracter_especial = findViewById(R.id.caracter_especial);
        letra_maiscula = findViewById(R.id.letra_maiscula);
        letra_minuscula = findViewById(R.id.letra_minuscula);

        registroUserDAO = new RegistroUserDAO(CriarActivity.this);
    }

    private void configurarListeners() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CriarActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
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
                    minimo.setText("✓ Mínimo de 6 caracteres");
                    caracter_especial.setText("✓ Mínimo de um caracter especial");
                    letra_maiscula.setText("✓ Mínimo de uma letra maiúscula");
                    letra_minuscula.setText("✓ Mínimo de uma letra minúscula");
                    minimo.setTextColor(Color.GREEN);
                    caracter_especial.setTextColor(Color.GREEN);
                    letra_maiscula.setTextColor(Color.GREEN);
                    letra_minuscula.setTextColor(Color.GREEN);

                } else {
                    minimo.setTextColor(password.length() > 6 ? Color.GREEN : Color.RED);
                    caracter_especial.setTextColor(containsSpecialChar(password) ? Color.GREEN : Color.RED);
                    letra_maiscula.setTextColor(containsUpperCase(password) ? Color.GREEN : Color.RED);
                    letra_minuscula.setTextColor(containsLowerCase(password) ? Color.GREEN : Color.RED);

                    // Atualizar textos com base na validação
                    minimo.setText(password.length() > 6 ? "✓ Mínimo de 6 caracteres" : "✗ Mínimo de 6 caracteres");
                    caracter_especial.setText(containsSpecialChar(password) ? "✓ Mínimo de um caracter especial" : "✗ Mínimo de um caracter especial");
                    letra_maiscula.setText(containsUpperCase(password) ? "✓ Mínimo de uma letra maiúscula" : "✗ Mínimo de uma letra maiúscula");
                    letra_minuscula.setText(containsLowerCase(password) ? "✓ Mínimo de uma letra minúscula" : "✗ Mínimo de uma letra minúscula");
                }
            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarConta();
            }
        });
    }

    private void criarConta() {
        String nomeStr = nome.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String password = senha.getText().toString();

        // Validação do nome
        if (nomeStr.isEmpty()) {
            mensagemErrorApresentar("Campo nome obrigatório");
            nome.requestFocus();
            return;
        }

        if (nomeStr.length() < 2) {
            mensagemErrorApresentar("Nome deve ter pelo menos 2 caracteres");
            nome.requestFocus();
            return;
        }

        // Validação do email
        if (emailStr.isEmpty()) {
            mensagemErrorApresentar("Campo email obrigatório");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            mensagemErrorApresentar("Email inválido");
            email.requestFocus();
            return;
        }

        // Verificar se email já existe
        if (registroUserDAO.selectEmail(emailStr)) {
            mensagemErrorApresentar("Este email já está cadastrado.");
            email.requestFocus();
            return;
        }

        // Validação da senha
        if (password.isEmpty()) {
            mensagemErrorApresentar("Campo senha obrigatório");
            senha.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            mensagemErrorApresentar("Senha inválida. Verifique os requisitos.");
            senha.requestFocus();
            return;
        }

        // Criar usuário
        try {
            String hashedPassword = HashUtils.sha256(password);

            RegistroUserModel user = new RegistroUserModel();
            user.setNome(nomeStr);
            user.setEmail(emailStr);
            user.setSenha(hashedPassword);

            registroUserDAO.insert(user);

            // Mostrar mensagem de sucesso
            mostrarSucesso("Conta criada com sucesso!", nomeStr);

        } catch (Exception e) {
            mensagemErrorApresentar("Erro ao criar conta. Tente novamente.");
        }
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

    private void mostrarSucesso(String mensagem, String nomeUsuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CriarActivity.this);
        builder.setTitle("🎉 Bem-vindo, " + nomeUsuario + "!");
        builder.setMessage(mensagem);
        builder.setIcon(R.drawable.ic_check_circle);
        builder.setCancelable(false);
        builder.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(CriarActivity.this, LoginActivity.class);
                // Passar email para facilitar o login
                it.putExtra("email_cadastrado", email.getText().toString());
                startActivity(it);
                finish();
            }
        });
        builder.create().show();
    }

    private void mensagemErrorApresentar(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CriarActivity.this);
        builder.setMessage(mensagem);
        builder.setTitle("⚠️ Atenção");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(CriarActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
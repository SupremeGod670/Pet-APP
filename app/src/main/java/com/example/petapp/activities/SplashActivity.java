package com.example.petapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;

public class SplashActivity extends AppCompatActivity {

    private ImageView pets;
    private Animation anim;
    private SharedPreferences sharedPreferences;
    private RegistroUserDAO registroUserDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        pets = findViewById(R.id.pets);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        registroUserDAO = new RegistroUserDAO(this);

        anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animacao_splash);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                verificarLoginAutomatico();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated
            }
        });
        pets.setAnimation(anim);
        anim.start();
    }

    private void verificarLoginAutomatico() {
        String emailLogado = sharedPreferences.getString("email_logado", "");

        // Verifica se existe um email salvo e se é válido
        if (emailLogado != null && !emailLogado.trim().isEmpty()) {
            // Verifica se o usuário ainda existe no banco de dados
            if (registroUserDAO.selectEmail(emailLogado)) {
                // Usuário válido encontrado, redireciona para a tela de início
                redirecionarParaInicio();
                return;
            } else {
                // Email salvo não existe mais no banco, limpa os dados salvos
                limparDadosLogin();
            }
        }

        // Se chegou até aqui, não há usuário logado válido, vai para login
        redirecionarParaLogin();
    }

    private void redirecionarParaInicio() {
        Intent intent = new Intent(SplashActivity.this, InicioActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirecionarParaLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void limparDadosLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email_logado");
        editor.remove("usuario_logado");
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fecha a conexão com o banco de dados se necessário
        if (registroUserDAO != null) {
            registroUserDAO.Close();
        }
    }
}
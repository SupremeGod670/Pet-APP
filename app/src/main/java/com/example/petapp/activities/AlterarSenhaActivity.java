package com.example.petapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;
import com.example.petapp.utils.HashUtils;

public class AlterarSenhaActivity extends AppCompatActivity {

    private TextView voltar;
    private EditText editSenhaAtual, editNovaSenha, editConfirmarSenha;
    private ImageView toggleSenhaAtual, toggleNovaSenha, toggleConfirmarSenha;
    private Button btnSalvarSenha;

    private RegistroUserDAO registroUserDAO;
    private SharedPreferences sharedPreferences;
    private String emailLogado;

    private boolean senhaAtualVisivel = false;
    private boolean novaSenhaVisivel = false;
    private boolean confirmarSenhaVisivel = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_senha);

        inicializarComponentes();
        configurarListeners();
        carregarEmailUsuario();
    }

    private void inicializarComponentes() {
        voltar = findViewById(R.id.voltar);
        editSenhaAtual = findViewById(R.id.edit_senha_atual);
        editNovaSenha = findViewById(R.id.edit_nova_senha);
        editConfirmarSenha = findViewById(R.id.edit_confirmar_senha);
        toggleSenhaAtual = findViewById(R.id.toggle_senha_atual);
        toggleNovaSenha = findViewById(R.id.toggle_nova_senha);
        toggleConfirmarSenha = findViewById(R.id.toggle_confirmar_senha);
        btnSalvarSenha = findViewById(R.id.btn_salvar_senha);

        registroUserDAO = new RegistroUserDAO(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        emailLogado = sharedPreferences.getString("email_logado", "");
    }

    private void configurarListeners() {
        voltar.setOnClickListener(v -> finish());

        toggleSenhaAtual.setOnClickListener(v -> {
            senhaAtualVisivel = !senhaAtualVisivel;
            if (senhaAtualVisivel) {
                editSenhaAtual.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggleSenhaAtual.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editSenhaAtual.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggleSenhaAtual.setImageResource(R.drawable.ic_visibility);
            }
            editSenhaAtual.setSelection(editSenhaAtual.getText().length());
        });

        toggleNovaSenha.setOnClickListener(v -> {
            novaSenhaVisivel = !novaSenhaVisivel;
            if (novaSenhaVisivel) {
                editNovaSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggleNovaSenha.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editNovaSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggleNovaSenha.setImageResource(R.drawable.ic_visibility);
            }
            editNovaSenha.setSelection(editNovaSenha.getText().length());
        });

        toggleConfirmarSenha.setOnClickListener(v -> {
            confirmarSenhaVisivel = !confirmarSenhaVisivel;
            if (confirmarSenhaVisivel) {
                editConfirmarSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggleConfirmarSenha.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editConfirmarSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggleConfirmarSenha.setImageResource(R.drawable.ic_visibility);
            }
            editConfirmarSenha.setSelection(editConfirmarSenha.getText().length());
        });

        btnSalvarSenha.setOnClickListener(v -> alterarSenha());
    }

    private void carregarEmailUsuario() {
        if (emailLogado == null || emailLogado.trim().isEmpty()) {
            Toast.makeText(this, "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void alterarSenha() {
        String senhaAtual = editSenhaAtual.getText().toString().trim();
        String novaSenha = editNovaSenha.getText().toString().trim();
        String confirmarSenha = editConfirmarSenha.getText().toString().trim();

        if (TextUtils.isEmpty(senhaAtual)) {
            editSenhaAtual.setError("Senha atual é obrigatória");
            editSenhaAtual.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(novaSenha)) {
            editNovaSenha.setError("Nova senha é obrigatória");
            editNovaSenha.requestFocus();
            return;
        }

        if (novaSenha.length() < 6) {
            editNovaSenha.setError("Nova senha deve ter pelo menos 6 caracteres");
            editNovaSenha.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmarSenha)) {
            editConfirmarSenha.setError("Confirmação de senha é obrigatória");
            editConfirmarSenha.requestFocus();
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            editConfirmarSenha.setError("Senhas não coincidem");
            editConfirmarSenha.requestFocus();
            return;
        }

        if (senhaAtual.equals(novaSenha)) {
            editNovaSenha.setError("A nova senha deve ser diferente da atual");
            editNovaSenha.requestFocus();
            return;
        }

        try {
            RegistroUserModel usuario = registroUserDAO.getUsuarioByEmail(emailLogado);
            if (usuario == null) {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            String senhaDb = usuario.getSenha();
            String senhaAtualHash = HashUtils.sha256(senhaAtual);

            if (!senhaAtualHash.equals(senhaDb)) {
                editSenhaAtual.setError("Senha atual incorreta");
                editSenhaAtual.requestFocus();
                return;
            }

            String novaSenhaHash = HashUtils.sha256(novaSenha);
            registroUserDAO.updateSenha(emailLogado, novaSenhaHash);

            Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();

            editSenhaAtual.setText("");
            editNovaSenha.setText("");
            editConfirmarSenha.setText("");

            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            Log.e("AlterarSenhaActivity", "Erro ao alterar senha: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao alterar senha: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registroUserDAO != null) {
            registroUserDAO.Close();
        }
    }
}

package com.example.petapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;

public class ConfigActivity extends AppCompatActivity {

    private TextView voltar;
    private TextView txtNome, txtEmail, txtSenha;
    private Button btnEditarPerfil, btnAlterarSenha, btnSobre, btnLogout;
    private TextView txtVersao;

    private RegistroUserDAO registroUserDAO;
    private SharedPreferences sharedPreferences;
    private String emailLogado;
    private AlertDialog currentDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        voltar = findViewById(R.id.voltar);
        txtNome = findViewById(R.id.txt_nome);
        txtEmail = findViewById(R.id.txt_email);
        txtSenha = findViewById(R.id.txt_senha);
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil);
        btnAlterarSenha = findViewById(R.id.btn_alterar_senha);
        btnSobre = findViewById(R.id.btn_sobre);
        btnLogout = findViewById(R.id.btn_logout);
        txtVersao = findViewById(R.id.txt_versao);

        inicializarComponentes();
        configurarListeners();
        carregarDadosUsuario();
    }

    private void inicializarComponentes() {
        registroUserDAO = new RegistroUserDAO(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        emailLogado = sharedPreferences.getString("email_logado", "");
    }

    private void configurarListeners() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volta para a activity anterior
            }
        });

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar edição de perfil
                mostrarMensagem("Funcionalidade em desenvolvimento", "Esta funcionalidade será implementada em breve.");
            }
        });

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar alteração de senha
                mostrarMensagem("Funcionalidade em desenvolvimento", "Esta funcionalidade será implementada em breve.");
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSobreApp();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacaoLogout();
            }
        });
    }

    private void carregarDadosUsuario() {
        // Verifique se a activity ainda está ativa
        if (isFinishing() || isDestroyed()) {
            return;
        }

        // Debug: Verificar todas as chaves do SharedPreferences
        android.util.Log.d("ConfigActivity", "=== DEBUG SHARED PREFERENCES ===");
        java.util.Map<String, ?> allPrefs = sharedPreferences.getAll();
        for (java.util.Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            android.util.Log.d("ConfigActivity", "Chave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }
        android.util.Log.d("ConfigActivity", "Email logado obtido: '" + emailLogado + "'");

        // Verificar se existe email logado
        if (emailLogado == null || emailLogado.trim().isEmpty()) {
            android.util.Log.e("ConfigActivity", "Email logado está vazio ou nulo");
            if (!isFinishing() && !isDestroyed()) {
                mostrarErro("Email não encontrado no sistema. Faça login novamente.", true);
            }
            return;
        }

        try {
            android.util.Log.d("ConfigActivity", "Buscando usuário com email: '" + emailLogado + "'");

            // Debug: Verificar se o email existe no banco
            boolean emailExiste = registroUserDAO.selectEmail(emailLogado);
            android.util.Log.d("ConfigActivity", "Email existe no banco: " + emailExiste);

            // Buscar usuário no banco de dados
            RegistroUserModel usuarioLogado = registroUserDAO.getUsuarioByEmail(emailLogado);

            if (usuarioLogado != null) {
                android.util.Log.d("ConfigActivity", "Usuário encontrado!");
                android.util.Log.d("ConfigActivity", "ID: " + usuarioLogado.getId());
                android.util.Log.d("ConfigActivity", "Nome: '" + usuarioLogado.getNome() + "'");
                android.util.Log.d("ConfigActivity", "Email: '" + usuarioLogado.getEmail() + "'");

                String nome = usuarioLogado.getNome();
                if (nome == null || nome.trim().isEmpty()) {
                    nome = emailLogado.split("@")[0];
                    android.util.Log.d("ConfigActivity", "Nome estava vazio, usando: " + nome);
                }

                txtNome.setText(nome);
                txtEmail.setText(usuarioLogado.getEmail());
                txtSenha.setText("••••••••");

                android.util.Log.d("ConfigActivity", "Dados carregados com sucesso na interface");

            } else {
                android.util.Log.e("ConfigActivity", "Usuário retornado é NULL para o email: '" + emailLogado + "'");

                // Debug: Tentar buscar todos os usuários para ver o que tem no banco
                android.util.Log.d("ConfigActivity", "=== LISTANDO TODOS OS USUÁRIOS NO BANCO ===");
                // Vou criar um método para isso
                debugListarTodosUsuarios();

                if (!isFinishing() && !isDestroyed()) {
                    mostrarErro("Dados do usuário não encontrados no banco. Faça login novamente.", true);
                }
            }
        } catch (Exception e) {
            android.util.Log.e("ConfigActivity", "Erro ao carregar dados do usuário: " + e.getMessage(), e);
            e.printStackTrace();
            if (!isFinishing() && !isDestroyed()) {
                mostrarErro("Erro ao acessar os dados: " + e.getMessage(), true);
            }
        }
    }

    // Método para debug - listar todos os usuários
    private void debugListarTodosUsuarios() {
        try {
            registroUserDAO.Open();
            android.database.Cursor cursor = registroUserDAO.db.query(
                    "tb_user",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            android.util.Log.d("ConfigActivity", "Total de usuários no banco: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                    String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    android.util.Log.d("ConfigActivity", "Usuário encontrado - ID: " + id + ", Nome: '" + nome + "', Email: '" + email + "'");
                } while (cursor.moveToNext());
            }

            cursor.close();
            registroUserDAO.Close();
        } catch (Exception e) {
            android.util.Log.e("ConfigActivity", "Erro ao listar usuários: " + e.getMessage(), e);
            try {
                registroUserDAO.Close();
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    private void mostrarSobreApp() {
        // Feche qualquer dialog anterior
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sobre o Pet App");
        builder.setMessage("🐾 Pet App v1.0.0\n\n" +
                "Aplicativo para identificação e gerenciamento de pets.\n\n" +
                "Funcionalidades:\n" +
                "• Cadastro de pets\n" +
                "• Identificação por QR Code\n" +
                "• Gerenciamento de informações\n" +
                "• Controle de usuários\n\n" +
                "Desenvolvido para ajudar na identificação e cuidado dos seus pets.");
        builder.setIcon(R.drawable.ic_info);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    private void mostrarConfirmacaoLogout() {
        // Feche qualquer dialog anterior
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Saída");
        builder.setMessage("Tem certeza que deseja sair da sua conta?");
        builder.setIcon(R.drawable.ic_logout);
        builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realizarLogout();
                currentDialog = null;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    private void realizarLogout() {
        // Limpar dados salvos do usuário
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email_logado");
        editor.remove("usuario_logado");
        editor.apply();

        // Redirecionar para tela de login
        redirecionarParaLogin();
    }

    private void redirecionarParaLogin() {
        Intent intent = new Intent(ConfigActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarMensagem(String titulo, String mensagem) {
        // Feche qualquer dialog anterior
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        // Verifique se a activity ainda está ativa
        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    private void mostrarErro(String mensagem, boolean redirecionar) {
        // Feche qualquer dialog anterior
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        // Verifique se a activity ainda está ativa
        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erro");
        builder.setMessage(mensagem);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
                if (redirecionar) {
                    redirecionarParaLogin();
                }
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Feche qualquer dialog aberto
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
            currentDialog = null;
        }

        // Feche a conexão com o banco de dados se necessário
        if (registroUserDAO != null) {
            registroUserDAO.Close(); // Se o DAO tiver um método close()
        }
    }
}
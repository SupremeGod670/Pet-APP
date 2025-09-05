package com.example.petapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petapp.R;
import com.example.petapp.database.databaseUser.dao.RegistroUserDAO;
import com.example.petapp.database.databaseUser.model.RegistroUserModel;

public class EditarPerfilActivity extends AppCompatActivity {

    private TextView voltar;
    private ImageView imgFotoPerfil;
    private EditText editNome;
    private Button btnSelecionarFoto, btnSalvarPerfil;

    private RegistroUserDAO registroUserDAO;
    private SharedPreferences sharedPreferences;
    private String emailLogado;
    private String urlFotoPerfilAtual = null;

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final String TAG = "EditarPerfilActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil);

        inicializarComponentes();
        configurarListeners();
        carregarDadosUsuario();
    }

    private void inicializarComponentes() {
        voltar = findViewById(R.id.voltar);
        imgFotoPerfil = findViewById(R.id.img_foto_perfil);
        editNome = findViewById(R.id.edit_nome);
        btnSelecionarFoto = findViewById(R.id.btn_selecionar_foto);
        btnSalvarPerfil = findViewById(R.id.btn_salvar_perfil);

        registroUserDAO = new RegistroUserDAO(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        emailLogado = sharedPreferences.getString("email_logado", "");
    }

    private void configurarListeners() {
        voltar.setOnClickListener(v -> finish());

        btnSelecionarFoto.setOnClickListener(v -> checkPermissionAndPickImage());

        imgFotoPerfil.setOnClickListener(v -> checkPermissionAndPickImage());

        btnSalvarPerfil.setOnClickListener(v -> salvarPerfil());
    }

    private void carregarDadosUsuario() {
        if (emailLogado == null || emailLogado.trim().isEmpty()) {
            Toast.makeText(this, "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            RegistroUserModel usuario = registroUserDAO.getUsuarioByEmail(emailLogado);
            if (usuario != null) {
                editNome.setText(usuario.getNome() != null ? usuario.getNome() : "");

                // Carregar foto de perfil se existir
                if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
                    urlFotoPerfilAtual = usuario.getFotoPerfil();
                    carregarImagemPerfil(urlFotoPerfilAtual);
                } else {
                    imgFotoPerfil.setImageResource(R.drawable.ic_person);
                }
            } else {
                Toast.makeText(this, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao carregar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarImagemPerfil(String urlImagem) {
        try {
            Glide.with(this)
                    .load(Uri.parse(urlImagem))
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(imgFotoPerfil);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar imagem: " + e.getMessage(), e);
            imgFotoPerfil.setImageResource(R.drawable.ic_person);
        }
    }

    private void checkPermissionAndPickImage() {
        String[] permissions = getRequiredPermissions();

        if (hasAllPermissions(permissions)) {
            escolherImagem();
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private String[] getRequiredPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
    }

    private boolean hasAllPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void escolherImagem() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(Intent.createChooser(intent, "Selecione uma foto"), IMAGE_PICK_CODE);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir seletor de imagens", e);
            Toast.makeText(this, "Erro ao abrir seletor de imagens", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null && data.getData() != null) {
            processarImagemSelecionada(data.getData());
        }
    }

    private void processarImagemSelecionada(Uri imageUri) {
        try {
            // Tentar obter permissão persistente
            try {
                getContentResolver().takePersistableUriPermission(imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException e) {
                Log.w(TAG, "Não foi possível obter permissão persistente para a URI", e);
            }

            // Carregar imagem usando Glide
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(imgFotoPerfil);

            // Armazenar a URI como string
            urlFotoPerfilAtual = imageUri.toString();

            Log.d(TAG, "Foto selecionada: " + urlFotoPerfilAtual);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao processar imagem selecionada", e);
            Toast.makeText(this, "Erro ao carregar imagem selecionada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
                escolherImagem();
            } else {
                Toast.makeText(this, "Permissão necessária para acessar imagens", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void salvarPerfil() {
        String novoNome = editNome.getText().toString().trim();

        if (!validarNome(novoNome)) {
            return;
        }

        try {
            // Atualizar perfil no banco
            registroUserDAO.updatePerfil(emailLogado, novoNome, urlFotoPerfilAtual);

            Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();

            // Voltar para a tela anterior
            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar perfil: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao salvar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarNome(String nome) {
        if (TextUtils.isEmpty(nome)) {
            editNome.setError("Nome é obrigatório");
            editNome.requestFocus();
            return false;
        }

        if (nome.length() < 2) {
            editNome.setError("Nome deve ter pelo menos 2 caracteres");
            editNome.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Fechar conexão com o banco se necessário
        if (registroUserDAO != null) {
            registroUserDAO.Close();
        }
    }
}
package com.example.petapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petapp.R;
import com.example.petapp.views.SignatureView;

public class FullScreenSignatureActivity extends AppCompatActivity {

    private static final String TAG = "FullScreenSignature";
    private SignatureView signatureView;
    private Button btnSalvar, btnLimpar, btnCancelar;

    public static final String EXTRA_SIGNATURE_DATA = "signature_data";
    public static final String RESULT_SIGNATURE_DATA = "result_signature_data";
    public static final int REQUEST_CODE_SIGNATURE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_signature);

        signatureView = findViewById(R.id.signature_view_fullscreen);
        btnSalvar = findViewById(R.id.btn_salvar_assinatura);
        btnLimpar = findViewById(R.id.btn_limpar_assinatura);
        btnCancelar = findViewById(R.id.btn_cancelar_assinatura);

        // Verificar se é modo somente visualização
        boolean viewOnly = getIntent().getBooleanExtra("VIEW_ONLY", false);

        // Carregar assinatura existente se houver
        String existingSignature = getIntent().getStringExtra(EXTRA_SIGNATURE_DATA);
        if (existingSignature != null && !existingSignature.isEmpty()) {
            signatureView.setSignatureFromString(existingSignature);
        }

        // Configurar modo de visualização
        if (viewOnly) {
            signatureView.setEnabled(false); // Desabilita desenho
            btnSalvar.setVisibility(View.GONE);
            btnLimpar.setVisibility(View.GONE);
            btnCancelar.setText("Fechar");
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarAssinatura();
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearSignature();
                Toast.makeText(FullScreenSignatureActivity.this, "Assinatura limpa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void salvarAssinatura() {
        if (signatureView.isEmpty()) {
            Toast.makeText(this, "Por favor, desenhe sua assinatura antes de salvar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String signatureData = signatureView.getSignatureAsString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_SIGNATURE_DATA, signatureData);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Salvar assinatura temporariamente
        String tempSignature = null;
        if (signatureView != null && !signatureView.isEmpty()) {
            tempSignature = signatureView.getSignatureAsString();
        }

        // Recriar a view
        if (tempSignature != null) {
            final String signature = tempSignature;
            signatureView.post(new Runnable() {
                @Override
                public void run() {
                    signatureView.setSignatureFromString(signature);
                }
            });
        }
    }
}
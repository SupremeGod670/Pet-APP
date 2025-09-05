package com.example.petapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.petapp.R;
import com.example.petapp.database.databasePet.dao.RegistroPetDAO;
import com.example.petapp.database.databasePet.model.RegistroPetModel;
import com.example.petapp.fragments.Fragment1;
import com.example.petapp.fragments.Fragment2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MostrarPetActivity extends AppCompatActivity {

    private TextView voltar;
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    private Button editarpet, downloadc, deletarpet;
    private FragmentStateAdapter pagerAdapter;
    private RegistroPetModel petAtual;
    private RegistroPetDAO dao;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static final String EXTRA_PET_ID = "EXTRA_PET_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pet);

        voltar = findViewById(R.id.voltar);
        viewPager = findViewById(R.id.rolar);
        editarpet = findViewById(R.id.editarpet);
        downloadc = findViewById(R.id.downloadc);
        deletarpet = findViewById(R.id.deletarpet);

        // Buscar dados do pet
        Long petId = getIntent().getLongExtra("PET_ID", -1L);
        if (petId != -1L) {
            RegistroPetDAO dao = new RegistroPetDAO(this);
            petAtual = dao.getPetById(petId);
        }

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarPetActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pagerAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        Fragment1 fragment1 = new Fragment1();
                        Bundle bundle1 = new Bundle();
                        if (petAtual != null) {
                            bundle1.putString("pet_name", petAtual.getNomepet());
                            bundle1.putString("pet_image_url", petAtual.getUrlImagem());
                        }
                        fragment1.setArguments(bundle1);
                        return fragment1;
                    case 1:
                        Fragment2 fragment2 = new Fragment2();
                        Bundle bundle2 = new Bundle();
                        if (petAtual != null) {
                            bundle2.putString("pet_name", petAtual.getNomepet());
                            bundle2.putString("pet_race", petAtual.getRaca());
                            bundle2.putString("pet_species", petAtual.getEspecie());
                            bundle2.putString("pet_sex", petAtual.getSexo());
                            bundle2.putString("pet_color", petAtual.getCor());
                            bundle2.putString("pet_city", petAtual.getCidade());
                            bundle2.putString("pet_state", petAtual.getEstado());
                            bundle2.putString("pet_neighborhood", petAtual.getBairro());
                            bundle2.putString("pet_address", petAtual.getEndereco());
                            bundle2.putString("pet_email", petAtual.getEmail());
                            bundle2.putString("pet_father", petAtual.getPai());
                            bundle2.putString("pet_mother", petAtual.getMae());
                            bundle2.putString("pet_birthplace", petAtual.getNaturalidade());
                            bundle2.putString("pet_description", petAtual.getDescricao());

                            if (petAtual.getCep() != null) {
                                bundle2.putString("pet_cep", String.valueOf(petAtual.getCep().longValue()));
                            }
                            if (petAtual.getTelefoneresd() != null) {
                                bundle2.putString("pet_phone", String.valueOf(petAtual.getTelefoneresd().longValue()));
                            }
                            if (petAtual.getTelefonecel() != null) {
                                bundle2.putString("pet_cell", String.valueOf(petAtual.getTelefonecel().longValue()));
                            }
                            if (petAtual.getNascimento() != null) {
                                bundle2.putString("pet_birth", String.valueOf(petAtual.getNascimento().longValue()));
                            }
                        }
                        fragment2.setArguments(bundle2);
                        return fragment2;
                    default:
                        return null;
                }
            }
        };
        viewPager.setAdapter(pagerAdapter);

        editarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petAtual != null) {
                    Intent intent = new Intent(MostrarPetActivity.this, CriarPetsActivity.class);

                    // Indicar que é uma edição
                    intent.putExtra("EDIT_MODE", true);
                    intent.putExtra("PET_ID", petAtual.getId());

                    // Passar todos os dados do pet
                    intent.putExtra("PET_NOME", petAtual.getNomepet());
                    intent.putExtra("PET_RACA", petAtual.getRaca());
                    intent.putExtra("PET_ESPECIE", petAtual.getEspecie());
                    intent.putExtra("PET_SEXO", petAtual.getSexo());
                    intent.putExtra("PET_COR", petAtual.getCor());
                    intent.putExtra("PET_CIDADE", petAtual.getCidade());
                    intent.putExtra("PET_ESTADO", petAtual.getEstado());
                    intent.putExtra("PET_BAIRRO", petAtual.getBairro());
                    intent.putExtra("PET_ENDERECO", petAtual.getEndereco());
                    intent.putExtra("PET_EMAIL", petAtual.getEmail());
                    intent.putExtra("PET_PAI", petAtual.getPai());
                    intent.putExtra("PET_MAE", petAtual.getMae());
                    intent.putExtra("PET_NATURALIDADE", petAtual.getNaturalidade());
                    intent.putExtra("PET_DESCRICAO", petAtual.getDescricao());
                    intent.putExtra("PET_URL_IMAGEM", petAtual.getUrlImagem());

                    // Dados numéricos (verificar se não são nulos)
                    if (petAtual.getCep() != null) {
                        intent.putExtra("PET_CEP", petAtual.getCep().longValue());
                    }
                    if (petAtual.getTelefoneresd() != null) {
                        intent.putExtra("PET_TELEFONE_RESD", petAtual.getTelefoneresd().longValue());
                    }
                    if (petAtual.getTelefonecel() != null) {
                        intent.putExtra("PET_TELEFONE_CEL", petAtual.getTelefonecel().longValue());
                    }
                    if (petAtual.getNascimento() != null) {
                        intent.putExtra("PET_NASCIMENTO", petAtual.getNascimento().longValue());
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(MostrarPetActivity.this, "Erro: dados do pet não encontrados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deletarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao = new RegistroPetDAO(MostrarPetActivity.this);
                dao.delete(petId);
                finish();
                Toast.makeText(MostrarPetActivity.this, "Pet deletado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        // Implementação do download PDF
        downloadc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    createAndSharePDF();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createAndSharePDF();
            } else {
                Toast.makeText(this, "Permissão necessária para salvar o arquivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createAndSharePDF() {
        if (petAtual == null) {
            Toast.makeText(this, "Dados do pet não encontrados", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar documento PDF
        PdfDocument pdfDocument = new PdfDocument();

        // Criar páginas (frente e verso)
        createPDFPage1(pdfDocument); // Frente - com foto
        createPDFPage2(pdfDocument); // Verso - com dados

        // Salvar arquivo
        String fileName = "RG_Pet_" + petAtual.getNomepet().replaceAll(" ", "_") + ".pdf";
        File file = savePDFToStorage(pdfDocument, fileName);

        pdfDocument.close();

        if (file != null) {
            sharePDF(file);
        }
    }

    private void createPDFPage1(PdfDocument pdfDocument) {
        // Criar página A4 (595 x 842 pontos)
        PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page1 = pdfDocument.startPage(pageInfo1);
        Canvas canvas = page1.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Fundo verde
        paint.setColor(Color.parseColor("#4CAF50"));
        canvas.drawRect(0, 0, 595, 842, paint);

        // Cabeçalho
        paint.setColor(Color.WHITE);
        paint.setTextSize(12);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🐾 REPÚBLICA FEDERATIVA DOS ANIMAIS 🐾", 297, 50, paint);

        // Card principal
        paint.setColor(Color.parseColor("#E8F5E8"));
        canvas.drawRoundRect(50, 80, 545, 600, 15, 15, paint);

        // Título do documento
        paint.setColor(Color.parseColor("#2E7D32"));
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("BRASIL", 297, 130, paint);
        canvas.drawText("CARTEIRA DE IDENTIDADE ANIMAL", 297, 150, paint);

        // Área da foto (placeholder)
        paint.setColor(Color.parseColor("#C8E6C9"));
        canvas.drawRect(80, 200, 280, 400, paint);

        paint.setColor(Color.WHITE);
        canvas.drawRect(90, 210, 270, 390, paint);

        // Baixar e exibir a imagem do pet
        if (petAtual != null && petAtual.getUrlImagem() != null && !petAtual.getUrlImagem().isEmpty()) {
            try {
                Uri imageUri;
                // Verifica se a URL é um content URI (provavelmente da galeria)
                if (petAtual.getUrlImagem().startsWith("content://")) {
                    imageUri = Uri.parse(petAtual.getUrlImagem());
                } else {
                    // Assume que é um caminho de arquivo e usa FileProvider
                    File imageFile = new File(petAtual.getUrlImagem());
                    imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imageFile);
                }
                ImageDecoder.Source source = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    source = ImageDecoder.createSource(getContentResolver(), imageUri); // Adiciona a flag aqui
                }
                Bitmap hardwareBitmap = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    hardwareBitmap = ImageDecoder.decodeBitmap(source);
                }
                // Convert to software bitmap
                Bitmap softwareBitmap = hardwareBitmap.copy(Bitmap.Config.ARGB_8888, false);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(softwareBitmap, 180, 180, true);
                canvas.drawBitmap(scaledBitmap, 90, 210, paint);
                // After drawing, recycle the bitmap if it's no longer needed and it's mutable
                if (hardwareBitmap != null && !hardwareBitmap.isRecycled()) {
                    hardwareBitmap.recycle();
                }
            } catch (FileNotFoundException e) {
                // Handle the case where the image file is not found
                drawPlaceholderFoto(canvas, paint); // Desenha "FOTO" se a imagem não for encontrada
                e.printStackTrace();
            } catch (IOException e) {
                drawPlaceholderFoto(canvas, paint); // Desenha "FOTO" em caso de outros erros de IO
                e.printStackTrace();
            }
        } else {
            drawPlaceholderFoto(canvas, paint); // Desenha "FOTO" se não houver URL da imagem
        }

        // Área das patinhas
        paint.setColor(Color.parseColor("#4CAF50"));
        canvas.drawRect(300, 200, 500, 400, paint);

        // Patinhas (emoji simulado com texto)
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🐾", 400, 250, paint);
        canvas.drawText("🐾", 400, 320, paint);
        canvas.drawText("🐾", 400, 390, paint);

        // Linha para assinatura
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);
        canvas.drawLine(80, 500, 515, 500, paint);

        paint.setTextSize(12);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Assinatura do titular", 297, 520, paint);

        // Patinhas na parte inferior
        paint.setColor(Color.WHITE);
        paint.setTextSize(10);
        canvas.drawText("🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾", 297, 780, paint);

        pdfDocument.finishPage(page1);
    }

    private void drawPlaceholderFoto(Canvas canvas, Paint paint) {
        // Texto "FOTO" no meio da área da foto
        paint.setColor(Color.GRAY);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("FOTO", 180, 310, paint);
    }

    private void createPDFPage2(PdfDocument pdfDocument) {
        // Criar segunda página
        PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
        PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);
        Canvas canvas = page2.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Fundo verde
        paint.setColor(Color.parseColor("#4CAF50"));
        canvas.drawRect(0, 0, 595, 842, paint);

        // Patinhas no topo
        paint.setColor(Color.WHITE);
        paint.setTextSize(8);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾", 297, 30, paint);

        // Cabeçalho "VÁLIDO EM TODO TERRITÓRIO NACIONAL"
        paint.setColor(Color.WHITE);
        canvas.drawRect(50, 50, 545, 80, paint);

        paint.setColor(Color.parseColor("#2E7D32"));
        paint.setTextSize(12);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("VÁLIDO EM TODO TERRITÓRIO NACIONAL", 297, 70, paint);

        // Card principal
        paint.setColor(Color.parseColor("#E8F5E8"));
        canvas.drawRoundRect(50, 100, 545, 750, 15, 15, paint);

        // Dados do pet
        drawPetInfo(canvas, paint);

        // Patinhas na parte inferior
        paint.setColor(Color.WHITE);
        paint.setTextSize(8);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾🐾", 297, 800, paint);

        pdfDocument.finishPage(page2);
    }

    private void drawPetInfo(Canvas canvas, Paint paint) {
        int startY = 140;
        int lineHeight = 25;
        int currentY = startY;

        paint.setTextAlign(Paint.Align.LEFT);

        // Coluna esquerda
        paint.setColor(Color.parseColor("#2E7D32"));
        paint.setTextSize(10);

        drawInfoLine(canvas, paint, "NOME:", safeGetString(petAtual.getNomepet()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "NASCIMENTO:", formatBirthDate(petAtual.getNascimento()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "ESPÉCIE:", safeGetString(petAtual.getEspecie()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "SEXO:", safeGetString(petAtual.getSexo()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "ENDEREÇO:", safeGetString(petAtual.getEndereco()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "BAIRRO:", safeGetString(petAtual.getBairro()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "CIDADE:", safeGetString(petAtual.getCidade()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "TEL. RESD.:", formatPhone(petAtual.getTelefoneresd()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "E-MAIL:", safeGetString(petAtual.getEmail()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "PAI:", safeGetString(petAtual.getPai()), 70, 320, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "MÃE:", safeGetString(petAtual.getMae()), 70, 320, currentY);

        // Coluna direita
        currentY = startY;

        drawInfoLine(canvas, paint, "RAÇA:", safeGetString(petAtual.getRaca()), 320, 520, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "NATURAL DE:", safeGetString(petAtual.getNaturalidade()), 320, 520, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "COR:", safeGetString(petAtual.getCor()), 320, 520, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "CEP:", formatCep(petAtual.getCep()), 320, 520, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "ESTADO:", safeGetString(petAtual.getEstado()), 320, 520, currentY);
        currentY += lineHeight;

        drawInfoLine(canvas, paint, "TEL. CEL.:", formatCellPhone(petAtual.getTelefonecel()), 320, 520, currentY);

        // Área de descrição
        paint.setColor(Color.parseColor("#C8E6C9"));
        canvas.drawRect(70, 450, 520, 550, paint);

        paint.setColor(Color.parseColor("#2E7D32"));
        paint.setTextSize(10);
        canvas.drawText("DESCRIÇÃO:", 80, 470, paint);

        paint.setColor(Color.parseColor("#333333"));
        String description = safeGetString(petAtual.getDescricao());
        if (description.length() > 80) {
            description = description.substring(0, 77) + "...";
        }
        canvas.drawText(description, 80, 490, paint);
    }

    private void drawInfoLine(Canvas canvas, Paint paint, String label, String value, int labelX, int valueX, int y) {
        paint.setColor(Color.parseColor("#2E7D32"));
        canvas.drawText(label, labelX, y, paint);

        paint.setColor(Color.parseColor("#333333"));
        canvas.drawText(value, labelX + 80, y, paint);
    }

    private String safeGetString(String value) {
        return value != null ? value : "";
    }

    private String formatBirthDate(Double birth) {
        if (birth == null || birth == 0) return "";
        String birthStr = String.valueOf(birth.longValue());
        if (birthStr.length() == 8) {
            return birthStr.substring(0, 2) + "/" + birthStr.substring(2, 4) + "/" + birthStr.substring(4);
        }
        return birthStr;
    }

    private String formatPhone(Double phone) {
        if (phone == null || phone == 0) return "";
        String phoneStr = String.valueOf(phone.longValue());
        if (phoneStr.length() == 10) {
            return "(" + phoneStr.substring(0, 2) + ") " + phoneStr.substring(2, 6) + "-" + phoneStr.substring(6);
        }
        return phoneStr;
    }

    private String formatCellPhone(Double cell) {
        if (cell == null || cell == 0) return "";
        String cellStr = String.valueOf(cell.longValue());
        if (cellStr.length() == 11) {
            return "(" + cellStr.substring(0, 2) + ") " + cellStr.substring(2, 7) + "-" + cellStr.substring(7);
        }
        return cellStr;
    }

    private String formatCep(Double cep) {
        if (cep == null || cep == 0) return "";
        String cepStr = String.valueOf(cep.longValue());
        if (cepStr.length() == 8) {
            return cepStr.substring(0, 5) + "-" + cepStr.substring(5);
        }
        return cepStr;
    }

    private File savePDFToStorage(PdfDocument pdfDocument, String fileName) {
        // Use o diretório de cache interno do aplicativo para evitar problemas de permissão e FileProvider
        File cachePath = new File(getApplicationContext().getCacheDir(), "pdfs");
        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }
        File file = new File(cachePath, fileName);

        try {
            // Salvar no diretório Downloads
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            // Crie o arquivo no diretório de downloads para o usuário
            File publicFile = new File(downloadsDir, fileName);

            // Primeiro, salve no cache interno
            file = new File(cachePath, fileName);


            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            fos.close();

            Toast.makeText(this, "PDF salvo em Downloads: " + fileName, Toast.LENGTH_LONG).show();

            // Copie do cache para o diretório de downloads público, se necessário,
            // mas o compartilhamento usará o URI do FileProvider do cache.
            // Esta etapa é mais para o usuário encontrar o arquivo facilmente.
            try (FileOutputStream publicFos = new FileOutputStream(publicFile);
                 java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    publicFos.write(buffer, 0, length);
                }
            } catch (IOException e) {
                // Log ou mostre um erro se a cópia para downloads públicos falhar, mas o arquivo ainda está no cache para compartilhamento
                e.printStackTrace();
                Toast.makeText(this, "Erro ao copiar PDF para Downloads: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null; // Retorna nulo em caso de erro ao salvar no cache
        }

        return file;
    }

    private void sharePDF(File file) {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RG do Pet - " + petAtual.getNomepet());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Compartilhando o RG do meu pet " + petAtual.getNomepet());
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Compartilhar RG do Pet"));
    }

    public RegistroPetModel getPetAtual() {
        return petAtual;
    }
}
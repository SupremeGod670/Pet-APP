package com.example.petapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;

public class SignatureDisplayView extends View {

    private Bitmap signatureBitmap;
    private Paint paint;

    public SignatureDisplayView(Context context) {
        super(context);
        init();
    }

    public SignatureDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (signatureBitmap != null && !signatureBitmap.isRecycled()) {
            // Calcular a escala para manter a proporção
            float scaleX = (float) getWidth() / signatureBitmap.getWidth();
            float scaleY = (float) getHeight() / signatureBitmap.getHeight();
            float scale = Math.min(scaleX, scaleY);

            // Calcular posição para centralizar
            float scaledWidth = signatureBitmap.getWidth() * scale;
            float scaledHeight = signatureBitmap.getHeight() * scale;
            float x = (getWidth() - scaledWidth) / 2;
            float y = (getHeight() - scaledHeight) / 2;

            // Criar bitmap escalado
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                    signatureBitmap,
                    (int) scaledWidth,
                    (int) scaledHeight,
                    true
            );

            // Desenhar bitmap
            canvas.drawBitmap(scaledBitmap, x, y, paint);

            // Limpar bitmap escalado se for diferente do original
            if (scaledBitmap != signatureBitmap && !scaledBitmap.isRecycled()) {
                scaledBitmap.recycle();
            }
        } else {
            // Desenhar texto placeholder se não houver assinatura
            paint.setColor(Color.GRAY);
            paint.setTextSize(16);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Sem assinatura", getWidth() / 2f, getHeight() / 2f, paint);
        }
    }

    public void setSignatureFromString(String signatureString) {
        if (signatureString == null || signatureString.isEmpty()) {
            clearSignature();
            return;
        }

        try {
            byte[] decodedString = Base64.decode(signatureString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (bitmap != null) {
                // Limpar bitmap anterior
                if (signatureBitmap != null && !signatureBitmap.isRecycled()) {
                    signatureBitmap.recycle();
                }

                signatureBitmap = bitmap;
                invalidate();
                setVisibility(View.VISIBLE);
            } else {
                clearSignature();
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearSignature();
        }
    }

    public void clearSignature() {
        if (signatureBitmap != null && !signatureBitmap.isRecycled()) {
            signatureBitmap.recycle();
        }
        signatureBitmap = null;
        invalidate();
        setVisibility(View.GONE);
    }

    public boolean hasSignature() {
        return signatureBitmap != null && !signatureBitmap.isRecycled();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearSignature();
    }
}
package com.example.petapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SignatureView extends View {

    private Paint paint;
    private Path path;
    private List<Path> paths;
    private List<Paint> paints;
    private float touchX, touchY;
    private static final float TOUCH_TOLERANCE = 4;

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3f);

        path = new Path();
        paths = new ArrayList<>();
        paints = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenhar todos os caminhos salvos
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        // Desenhar o caminho atual
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    private void touchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        touchX = x;
        touchY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - touchX);
        float dy = Math.abs(y - touchY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(touchX, touchY, (x + touchX) / 2, (y + touchY) / 2);
            touchX = x;
            touchY = y;
        }
    }

    private void touchUp() {
        path.lineTo(touchX, touchY);

        // Salvar o caminho atual
        Paint newPaint = new Paint(paint);
        paths.add(new Path(path));
        paints.add(newPaint);

        // Resetar para o próximo desenho
        path.reset();
    }

    public void clearSignature() {
        paths.clear();
        paints.clear();
        path.reset();
        invalidate();
    }

    public boolean isEmpty() {
        return paths.isEmpty();
    }

    public Bitmap getSignatureBitmap() {
        if (isEmpty()) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Fundo branco
        canvas.drawColor(Color.WHITE);

        // Desenhar todas as assinaturas
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        return bitmap;
    }

    public String getSignatureAsString() {
        Bitmap bitmap = getSignatureBitmap();
        if (bitmap == null) {
            return null;
        }

        // Converter bitmap para string base64
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    public void setSignatureFromString(String signatureString) {
        if (signatureString == null || signatureString.isEmpty()) {
            clearSignature();
            return;
        }

        try {
            byte[] decodedString = android.util.Base64.decode(signatureString, android.util.Base64.DEFAULT);
            Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (bitmap != null) {
                clearSignature();

                // Desenhar o bitmap no canvas
                Canvas canvas = new Canvas();
                Paint bitmapPaint = new Paint();
                bitmapPaint.setAntiAlias(true);

                // Criar um path que represente a assinatura
                Path signaturePath = new Path();
                Paint signaturePaint = new Paint(paint);

                // Adicionar o path e paint às listas
                paths.add(signaturePath);
                paints.add(signaturePaint);

                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearSignature();
        }
    }
}
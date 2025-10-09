package com.example.petapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SignatureView extends View {

    private static final String TAG = "SignatureView";
    private Paint paint;
    private Path path;
    private List<Path> paths;
    private List<Paint> paints;
    private float touchX, touchY;
    private static final float TOUCH_TOLERANCE = 4;

    // Bitmap para armazenar assinatura carregada de string
    private Bitmap loadedSignatureBitmap;

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

        // Se há um bitmap carregado, desenhar primeiro
        if (loadedSignatureBitmap != null && !loadedSignatureBitmap.isRecycled()) {
            canvas.drawBitmap(loadedSignatureBitmap, 0, 0, null);
        }

        // Desenhar todos os caminhos salvos
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        // Desenhar o caminho atual
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

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

        if (loadedSignatureBitmap != null && !loadedSignatureBitmap.isRecycled()) {
            loadedSignatureBitmap.recycle();
            loadedSignatureBitmap = null;
        }

        invalidate();
    }

    public boolean isEmpty() {
        return paths.isEmpty() && loadedSignatureBitmap == null;
    }

    public Bitmap getSignatureBitmap() {
        if (isEmpty()) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                getWidth() > 0 ? getWidth() : 500,
                getHeight() > 0 ? getHeight() : 300,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);

        // Fundo branco
        canvas.drawColor(Color.WHITE);

        // Desenhar bitmap carregado se existir
        if (loadedSignatureBitmap != null && !loadedSignatureBitmap.isRecycled()) {
            canvas.drawBitmap(loadedSignatureBitmap, 0, 0, null);
        }

        // Desenhar todas as assinaturas
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }

        return bitmap;
    }

    public String getSignatureAsString() {
        Bitmap bitmap = getSignatureBitmap();
        if (bitmap == null) {
            Log.w(TAG, "getSignatureAsString: bitmap é null");
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d(TAG, "getSignatureAsString: sucesso, length = " + result.length());

            // Não reciclar o bitmap aqui pois ainda pode ser usado
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao converter assinatura para string", e);
            return null;
        }
    }

    public void setSignatureFromString(String signatureString) {
        if (signatureString == null || signatureString.isEmpty()) {
            Log.w(TAG, "setSignatureFromString: string vazia ou null");
            clearSignature();
            return;
        }

        try {
            Log.d(TAG, "setSignatureFromString: recebido string com length = " + signatureString.length());

            byte[] decodedString = Base64.decode(signatureString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (bitmap != null) {
                Log.d(TAG, "setSignatureFromString: bitmap decodificado com sucesso");

                // Limpar assinatura anterior
                if (loadedSignatureBitmap != null && !loadedSignatureBitmap.isRecycled()) {
                    loadedSignatureBitmap.recycle();
                }

                // Limpar paths desenhados
                paths.clear();
                paints.clear();
                path.reset();

                // Escalar bitmap para o tamanho da view se necessário
                if (getWidth() > 0 && getHeight() > 0) {
                    loadedSignatureBitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            getWidth(),
                            getHeight(),
                            true
                    );
                    if (loadedSignatureBitmap != bitmap) {
                        bitmap.recycle();
                    }
                } else {
                    loadedSignatureBitmap = bitmap;
                }

                invalidate();
                Log.d(TAG, "setSignatureFromString: sucesso!");
            } else {
                Log.e(TAG, "setSignatureFromString: falha ao decodificar bitmap");
                clearSignature();
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar assinatura de string", e);
            clearSignature();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (loadedSignatureBitmap != null && !loadedSignatureBitmap.isRecycled()) {
            loadedSignatureBitmap.recycle();
            loadedSignatureBitmap = null;
        }
    }
}
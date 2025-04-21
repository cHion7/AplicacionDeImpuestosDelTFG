package com.example.aplicaciondeimpuestosdeltfg.gestor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class MultiDotSpan implements LineBackgroundSpan {

    private final int radius;
    private final int[] colors;

    public MultiDotSpan(int... colors) {
        this.radius = 6; // Puedes ajustar aquí el tamaño de los puntos
        this.colors = colors;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence charSequence, int start, int end, int lineNumber) {

        int spacing = 20; // Espacio horizontal entre puntos
        int totalWidth = colors.length * radius * 2 + (colors.length - 1) * spacing;
        int centerX = (left + right) / 2;
        int y = bottom + radius * 2;

        Paint.Style originalStyle = paint.getStyle();
        int originalColor = paint.getColor();

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            float cx = centerX - totalWidth / 2f + i * (radius * 2 + spacing) + radius;
            canvas.drawCircle(cx, y, radius, paint);
        }

        // Restaurar el estilo original del paint
        paint.setColor(originalColor);
        paint.setStyle(originalStyle);
    }
}

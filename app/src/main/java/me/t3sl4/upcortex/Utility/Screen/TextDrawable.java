package me.t3sl4.upcortex.Utility.Screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable {
    private final String text;
    private final Paint paint;
    private final Context context;

    public TextDrawable(Context context, String text, int textColor, float textSizeDp) {
        this.context = context;
        this.text = text;
        this.paint = new Paint();
        this.paint.setColor(textColor);
        this.paint.setTextSize(textSizeDp * context.getResources().getDisplayMetrics().density); // dp to pixels
        this.paint.setAntiAlias(true);
        this.paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float x = bounds.centerX();
        float y = bounds.centerY() - ((paint.descent() + paint.ascent()) / 2); // Align text vertically
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }
}
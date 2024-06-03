package me.t3sl4.upcortex.UI.Components.CircularCountdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import me.t3sl4.upcortex.R;

public class CircularCountdownView extends View {

    private Paint progressPaint;
    private Paint backgroundPaint;
    private Paint progressTextPaint;
    private Paint remainingTimeTextPaint;
    private RectF rectF;
    private long duration = 3600000; // Default: 1 hour in milliseconds
    private long remainingTime = 1800000; // Default: 30 minutes in milliseconds
    private String progressText = "Kalan Süre \n";
    private int progressColor;
    private int backgroundColor;
    private int progressTextColor;
    private int remainingTimeTextColor;
    private float progressTextSize;
    private float remainingTimeTextSize;
    private float strokeWidth;
    private float circleDiameter;
    private boolean clockwise = true;
    private boolean softProgress = false;

    public CircularCountdownView(Context context) {
        super(context);
        init(context, null);
    }

    public CircularCountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularCountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularCountdownView);
            progressColor = typedArray.getColor(R.styleable.CircularCountdownView_progressColor, ContextCompat.getColor(context, R.color.ratingColor));
            backgroundColor = typedArray.getColor(R.styleable.CircularCountdownView_backgroundColor, ContextCompat.getColor(context, R.color.secondaryColor));
            progressTextColor = typedArray.getColor(R.styleable.CircularCountdownView_progressTextColor, ContextCompat.getColor(context, R.color.ratingColor));
            remainingTimeTextColor = typedArray.getColor(R.styleable.CircularCountdownView_remainingTimeTextColor, ContextCompat.getColor(context, R.color.ratingColor));
            progressTextSize = typedArray.getDimension(R.styleable.CircularCountdownView_progressTextSize, 40);
            remainingTimeTextSize = typedArray.getDimension(R.styleable.CircularCountdownView_remainingTimeTextSize, 40);
            strokeWidth = typedArray.getDimension(R.styleable.CircularCountdownView_strokeWidth, 20);
            progressText = typedArray.getString(R.styleable.CircularCountdownView_progressText);
            duration = typedArray.getInt(R.styleable.CircularCountdownView_duration, 3600000);
            remainingTime = typedArray.getInt(R.styleable.CircularCountdownView_remainingTime, 1800000);
            circleDiameter = typedArray.getDimension(R.styleable.CircularCountdownView_circleDiameter, 200);
            clockwise = typedArray.getBoolean(R.styleable.CircularCountdownView_clockwise, true);
            softProgress = typedArray.getBoolean(R.styleable.CircularCountdownView_softProgress, false);
            typedArray.recycle();
        } else {
            progressColor = ContextCompat.getColor(context, R.color.ratingColor);
            backgroundColor = ContextCompat.getColor(context, R.color.secondaryColor);
            progressTextColor = ContextCompat.getColor(context, R.color.ratingColor);
            remainingTimeTextColor = ContextCompat.getColor(context, R.color.ratingColor);
            progressTextSize = 40;
            remainingTimeTextSize = 40;
            strokeWidth = 20;
            circleDiameter = 200;
        }

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);

        if (softProgress) {
            progressPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        progressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressTextPaint.setColor(progressTextColor);
        progressTextPaint.setTextSize(progressTextSize);
        progressTextPaint.setTextAlign(Paint.Align.CENTER);

        remainingTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        remainingTimeTextPaint.setColor(remainingTimeTextColor);
        remainingTimeTextPaint.setTextSize(remainingTimeTextSize);
        remainingTimeTextPaint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = progressPaint.getStrokeWidth() / 2;
        float diameter = Math.min(circleDiameter, Math.min(w, h));
        float left = (w - diameter) / 2;
        float top = (h - diameter) / 2;
        rectF.set(left + padding, top + padding, left + diameter - padding, top + diameter - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = 360 * remainingTime / duration;
        if (!clockwise) {
            angle = -angle;
        }
        canvas.drawOval(rectF, backgroundPaint);
        canvas.drawArc(rectF, -90, angle, false, progressPaint);

        String timeText = formatRemainingTime(remainingTime);
        canvas.drawText(progressText, getWidth() / 2, getHeight() / 2 - 40, progressTextPaint);
        canvas.drawText(timeText, getWidth() / 2, getHeight() / 2 + 40, remainingTimeTextPaint);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
        invalidate();
    }

    private String formatRemainingTime(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d Sa %02d Dk. %02d Sn.", hours, minutes, seconds);
    }
}

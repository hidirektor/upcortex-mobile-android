package me.t3sl4.upcortex.UI.Components.CircleStepper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.t3sl4.upcortex.R;

public class CircleStepper extends View {

    private int stepSize;
    private int currentStep;
    private boolean fill;
    private int circleColor;
    private float circleSize;
    private float borderSize;
    private boolean center;
    private float padding;
    private boolean pager;
    private Paint paint;

    public CircleStepper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleStepper,
                0, 0);

        try {
            stepSize = a.getInteger(R.styleable.CircleStepper_circleStepperStepSize, 3);
            currentStep = a.getInteger(R.styleable.CircleStepper_circleStepperCurrentStep, 0);
            fill = a.getBoolean(R.styleable.CircleStepper_circleStepperFill, true);
            circleColor = a.getColor(R.styleable.CircleStepper_circleStepperCircleColor, 0xFF000000);
            circleSize = a.getDimension(R.styleable.CircleStepper_circleStepperCircleSize, 50.0f);
            borderSize = a.getDimension(R.styleable.CircleStepper_circleStepperBorderSize, 5.0f);
            center = a.getBoolean(R.styleable.CircleStepper_circleStepperCenter, true);
            padding = a.getDimension(R.styleable.CircleStepper_circleStepperPadding, 10.0f);
            pager = a.getBoolean(R.styleable.CircleStepper_circleStepperPager, false);
        } finally {
            a.recycle();
        }

        paint = new Paint();
        paint.setColor(circleColor);
        paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        paint.setAntiAlias(true);  // Çemberlerin düzgün görünmesi için ekledik
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float totalWidth = stepSize * circleSize * 2 + (stepSize - 1) * padding;
        float startX = center ? (getWidth() - totalWidth) / 2 + circleSize : padding + circleSize;
        float centerY = getHeight() / 2f;

        for (int i = 0; i < stepSize; i++) {
            if (pager) {
                if (i == currentStep) {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                } else {
                    paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
                }
            } else {
                if (i < currentStep) {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                } else {
                    paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
                }
            }

            float x = startX + i * (circleSize * 2 + padding);

            // Çemberin sınır dışına çıkmasını önle
            if (x + circleSize > getWidth()) {
                x = getWidth() - circleSize;
            } else if (x - circleSize < 0) {
                x = circleSize;
            }

            canvas.drawCircle(x, centerY, circleSize, paint);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (stepSize * (circleSize * 2 + padding)) + getPaddingLeft() + getPaddingRight();
        int desiredHeight = (int) (circleSize * 2 + padding) + getPaddingTop() + getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    // Getter ve setter metodları

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
        invalidate();
        requestLayout();
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        invalidate();
        requestLayout();
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
        invalidate();
        requestLayout();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        paint.setColor(circleColor);
        invalidate();
        requestLayout();
    }

    public float getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(float circleSize) {
        this.circleSize = circleSize;
        invalidate();
        requestLayout();
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
        paint.setStrokeWidth(borderSize);
        invalidate();
        requestLayout();
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
        invalidate();
        requestLayout();
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
        invalidate();
        requestLayout();
    }
}
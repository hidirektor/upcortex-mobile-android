package me.t3sl4.upcortex.UI.Components.HorizontalStepper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import me.t3sl4.upcortex.R;

public class HorizontalStepper extends View {
    private int stepSize;
    private int currentStep;
    private int completedColor;
    private int currentColor;
    private int generalColor;
    private float leftTopRadius;
    private float leftBottomRadius;
    private float rightTopRadius;
    private float rightBottomRadius;
    private boolean pager;
    private float padding;

    private Paint paint;

    public HorizontalStepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalStepper, 0, 0);

        try {
            stepSize = a.getInt(R.styleable.HorizontalStepper_horizontalStepperStepSize, 5);
            currentStep = a.getInt(R.styleable.HorizontalStepper_horizontalStepperCurrentStep, 0);
            completedColor = a.getColor(R.styleable.HorizontalStepper_horizontalStepperCompletedColor, 0xFF00FF00); // Default Green
            currentColor = a.getColor(R.styleable.HorizontalStepper_horizontalStepperCurrentColor, 0xFFFF0000); // Default Red
            generalColor = a.getColor(R.styleable.HorizontalStepper_horizontalStepperGeneralColor, 0xFFCCCCCC); // Default Grey
            leftTopRadius = a.getDimension(R.styleable.HorizontalStepper_horizontalStepperLeftTopRadius, 0);
            leftBottomRadius = a.getDimension(R.styleable.HorizontalStepper_horizontalStepperLeftBottomRadius, 0);
            rightTopRadius = a.getDimension(R.styleable.HorizontalStepper_horizontalStepperRightTopRadius, 0);
            rightBottomRadius = a.getDimension(R.styleable.HorizontalStepper_horizontalStepperRightBottomRadius, 0);
            pager = a.getBoolean(R.styleable.HorizontalStepper_horizontalStepperPager, false);
            padding = a.getDimension(R.styleable.HorizontalStepper_horizontalStepperPadding, 0);
        } finally {
            a.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float stepWidth = width / (float) stepSize;

        for (int i = 0; i < stepSize; i++) {
            if (pager) {
                paint.setColor(i == currentStep ? currentColor : generalColor);
            } else {
                if (i < currentStep) {
                    paint.setColor(completedColor);
                } else if (i == currentStep) {
                    paint.setColor(currentColor);
                } else {
                    paint.setColor(generalColor);
                }
            }

            float left = i * stepWidth + padding;
            float right = left + stepWidth - padding;
            float top = padding;
            float bottom = height - padding;

            RectF rectF = new RectF(left, top, right, bottom);

            canvas.drawRoundRect(rectF, leftTopRadius, leftTopRadius, paint);
        }
    }

    public void setStep(int step) {
        if (step >= 0 && step < stepSize) {
            currentStep = step;
            invalidate();
        }
    }
}
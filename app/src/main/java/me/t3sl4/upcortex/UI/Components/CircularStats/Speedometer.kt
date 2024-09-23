package me.t3sl4.upcortex.UI.Components.CircularStats

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.animation.doOnEnd
import me.t3sl4.upcortex.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Speedometer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Attribute Defaults
    private var _maxPercent = 60

    @Dimension
    private var _borderSize = 36f

    @Dimension
    private var _textGap = 50f

    @ColorInt
    private var _borderColor = Color.parseColor("#402c47")

    @ColorInt
    private var _fillColor = Color.parseColor("#d83a78")

    @ColorInt
    private var _firstQuarterColor = Color.parseColor("#00ff00")  // 0-25%
    @ColorInt
    private var _secondQuarterColor = Color.parseColor("#ffff00")  // 26-50%
    @ColorInt
    private var _thirdQuarterColor = Color.parseColor("#ff7f00")   // 51-75%
    @ColorInt
    private var _fourthQuarterColor = Color.parseColor("#ff0000")  // 76-100%

    @ColorInt
    private var _textColor = Color.parseColor("#f5f5f5")

    @ColorInt
    private var _firstPercentColor = Color.parseColor("#f5f5f5")

    @ColorInt
    private var _secondPercentColor = Color.parseColor("#f5f5f5")

    private var _tickBorder = true
    private var _percentText = false
    private var _mode = 0  // 0: Tek renk, 1: Çeyrek renklendirme
    private var _firstPercent = 0  // Test için kullanılacak yüzde
    private var _secondPercent = 0  // Test için kullanılacak yüzde

    // Dynamic Values
    private val indicatorBorderRect = RectF()

    private val tickBorderRect = RectF()

    private val textBounds = Rect()

    private var angle = MIN_ANGLE

    private var speed = 0

    // Dimension Getters
    private val centerX get() = width / 2f

    private val centerY get() = height / 2f

    // Core Attributes
    var maxPercent: Int
        get() = _maxPercent
        set(value) {
            _maxPercent = value
            invalidate()
        }

    var borderSize: Float
        @Dimension get() = _borderSize
        set(@Dimension value) {
            _borderSize = value
            paintIndicatorBorder.strokeWidth = value
            paintIndicatorFill.strokeWidth = value
            invalidate()
        }

    var textGap: Float
        @Dimension get() = _textGap
        set(@Dimension value) {
            _textGap = value
            invalidate()
        }

    var tickBorder: Boolean
        get() = _tickBorder
        set(value) {
            _tickBorder = value
            invalidate()
        }

    var percentText: Boolean
        get() = _percentText
        set(value) {
            _percentText = value
            invalidate()
        }

    var borderColor: Int
        @ColorInt get() = _borderColor
        set(@ColorInt value) {
            _borderColor = value
            paintIndicatorBorder.color = value
            paintTickBorder.color = value
            paintMajorTick.color = value
            paintMinorTick.color = value
            invalidate()
        }

    var fillColor: Int
        @ColorInt get() = _fillColor
        set(@ColorInt value) {
            _fillColor = value
            paintIndicatorFill.color = value
            invalidate()
        }

    var textColor: Int
        @ColorInt get() = _textColor
        set(@ColorInt value) {
            _textColor = value
            paintTickText.color = value
            paintSpeed.color = value
            paintMetric.color = value
            invalidate()
        }

    var mode: Int
        get() = _mode
        set(value) {
            _mode = value
            invalidate()
        }

    var firstPercent: Int
        get() = _firstPercent
        set(value) {
            _firstPercent = value
            invalidate()
        }

    var secondPercent: Int
        get() = _secondPercent
        set(value) {
            _secondPercent = value
            invalidate()
        }

    var firstPercentColor: Int
        @ColorInt get() = _firstPercentColor
        set(@ColorInt value) {
            _firstPercentColor = value
            invalidate()
        }

    var secondPercentColor: Int
        @ColorInt get() = _secondPercentColor
        set(@ColorInt value) {
            _secondPercentColor = value
            invalidate()
        }

    var firstQuarterColor: Int
        @ColorInt get() = _firstQuarterColor
        set(@ColorInt value) {
            _firstQuarterColor = value
            invalidate()
        }

    var secondQuarterColor: Int
        @ColorInt get() = _secondQuarterColor
        set(@ColorInt value) {
            _secondQuarterColor = value
            invalidate()
        }

    var thirdQuarterColor: Int
        @ColorInt get() = _thirdQuarterColor
        set(@ColorInt value) {
            _thirdQuarterColor = value
            invalidate()
        }

    var fourthQuarterColor: Int
        @ColorInt get() = _fourthQuarterColor
        set(@ColorInt value) {
            _fourthQuarterColor = value
            invalidate()
        }

    // Paints
    private val paintIndicatorBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = borderSize
        strokeCap = Paint.Cap.ROUND
    }

    private val paintIndicatorFill = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = borderSize
        strokeCap = Paint.Cap.ROUND
    }

    private val paintIndicatorNeedle = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = borderSize
        strokeCap = Paint.Cap.ROUND
    }

    private val paintTickBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintMajorTick = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = MAJOR_TICK_WIDTH
        strokeCap = Paint.Cap.BUTT
    }

    private val paintMinorTick = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = borderColor
        strokeWidth = MINOR_TICK_WIDTH
        strokeCap = Paint.Cap.BUTT
    }

    private val paintTickText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 40f
    }

    private val paintSpeed = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 260f
    }

    private val paintMetric = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = textColor
        textSize = 50f
    }

    // Animators
    private val animator = ValueAnimator.ofFloat().apply {
        interpolator = AccelerateDecelerateInterpolator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        indicatorBorderRect.set(
            borderSize / 2,
            borderSize / 2,
            width - borderSize / 2,
            width - borderSize / 2
        )
        tickBorderRect.set(
            borderSize + TICK_MARGIN,
            borderSize + TICK_MARGIN,
            width - borderSize - TICK_MARGIN,
            width - borderSize - TICK_MARGIN
        )
    }

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Speedometer,
            defStyleAttr,
            0
        )

        try {
            with(typedArray) {
                maxPercent = getInt(
                    R.styleable.Speedometer_maxPercent,
                    maxPercent
                )
                borderSize = getDimension(
                    R.styleable.Speedometer_borderSize,
                    borderSize
                )
                textGap = getDimension(
                    R.styleable.Speedometer_textGap,
                    textGap
                )
                borderColor = getColor(
                    R.styleable.Speedometer_borderColor,
                    borderColor
                )
                fillColor = getColor(
                    R.styleable.Speedometer_fillColor,
                    borderColor
                )
                textColor = getColor(
                    R.styleable.Speedometer_textColor,
                    borderColor
                )
                tickBorder = getBoolean(
                    R.styleable.Speedometer_tickBorder,
                    tickBorder
                )
                percentText = getBoolean(
                    R.styleable.Speedometer_percentText,
                    percentText
                )
                mode = getInt(
                    R.styleable.Speedometer_mode,
                    mode
                )
                firstPercent = getInt(
                    R.styleable.Speedometer_firstPercent,
                    firstPercent
                )
                secondPercent = getInt(
                    R.styleable.Speedometer_secondPercent,
                    firstPercent
                )
                firstPercentColor = getInt(
                    R.styleable.Speedometer_firstPercentColor,
                    firstPercentColor
                )
                secondPercentColor = getInt(
                    R.styleable.Speedometer_secondPercentColor,
                    secondPercentColor
                )
                firstQuarterColor = getColor(
                    R.styleable.Speedometer_firstQuarterColor,
                    firstQuarterColor
                )
                secondQuarterColor = getColor(
                    R.styleable.Speedometer_secondQuarterColor,
                    secondQuarterColor
                )
                thirdQuarterColor = getColor(
                    R.styleable.Speedometer_thirthQuarterColor,
                    thirdQuarterColor
                )
                fourthQuarterColor = getColor(
                    R.styleable.Speedometer_fourthQuarterColor,
                    fourthQuarterColor
                )
            }
        } catch (e: Exception) {
            // ignored
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        renderMajorTicks(canvas)
        renderMinorTicks(canvas)
        renderBorder(canvas)
        if (mode == 0) {
            // Mode 0: Tek renk ile dolum
            renderBorderFill(canvas)
        } else {
            renderQuarterColors(canvas)  // Çeyrek renkleri render ediyoruz
            renderSpeedBars(canvas)       // Yüzde 87'yi gösteren siyah çubuğu çiziyoruz
        }
        if (tickBorder) {
            renderTickBorder(canvas)
        }
    }

    private fun renderQuarterColors(canvas: Canvas) {
        val quarterPercents = listOf(100, 75, 50, 25)
        val quarterColors = listOf(_fourthQuarterColor, _thirdQuarterColor, _secondQuarterColor, _firstQuarterColor)

        for (i in quarterColors.indices) {
            paintIndicatorFill.color = quarterColors[i]
            val sweepAngle = (quarterPercents[i] / 100f) * SWEEP_ANGLE
            canvas.drawArc(indicatorBorderRect, START_ANGLE, sweepAngle, false, paintIndicatorFill) // Çeyreği çiz
        }
    }

    private fun renderSpeedBars(canvas: Canvas) {
        val centerX = indicatorBorderRect.centerX()
        val centerY = indicatorBorderRect.centerY()

        val needleWidth = 30f // Adjust this to change the width of the needle
        val baseOffset = 50 // Offset from center for the needles (adjust as needed)
        val cornerRadius = 120f // Radius for rounded corners of the triangle

        // Step 1: Drawable kullanarak ikon_ellipse'i tam merkeze yerleştirin
        val ellipseDrawable: Drawable? = context.getDrawable(R.drawable.ikon_center_ellipse)
        val iconSize = 100 // İkonun boyutu (istenirse değiştirilebilir)

        ellipseDrawable?.let {
            // İkonun sol üst köşesini ve sağ alt köşesini hesaplayın
            val left = (centerX - iconSize / 2).toInt()
            val top = (centerY - iconSize / 2).toInt()
            val right = (centerX + iconSize / 2).toInt()
            val bottom = (centerY + iconSize / 2).toInt()

            // İkonun konumunu belirleyin
            it.setBounds(left, top, right, bottom)

            // İkonu canvas'a çizin
            it.draw(canvas)
        }

        // Step 2: Draw the First Percent Bar (First Triangle)
        if (firstPercent > 0) {
            paintIndicatorNeedle.color = firstPercentColor
            paintIndicatorNeedle.style = Paint.Style.FILL

            val firstSweepAngle = (firstPercent / 100f) * SWEEP_ANGLE
            val firstEndX = centerX + (centerX - borderSize - MAJOR_TICK_SIZE - baseOffset) * cos((START_ANGLE + firstSweepAngle).toRadian())
            val firstEndY = centerY + (centerY - borderSize - MAJOR_TICK_SIZE - baseOffset) * sin((START_ANGLE + firstSweepAngle).toRadian())

            val firstTrianglePath = createRoundedTrianglePath(
                centerX, centerY, firstEndX, firstEndY, needleWidth
            )

            // Drawing with rounded corners
            val roundedRect = RectF()
            firstTrianglePath.computeBounds(roundedRect, true)
            val roundedTrianglePath = Path()
            roundedTrianglePath.addRoundRect(roundedRect, cornerRadius, cornerRadius, Path.Direction.CW)

            canvas.drawPath(roundedTrianglePath, paintIndicatorNeedle)
        }
    }

    fun createRoundedTrianglePath(startX: Float, startY: Float, endX: Float, endY: Float, width: Float): Path {
        val path = Path()

        // Base center is (centerX, centerY)
        val baseLeftX = centerX - width / 2
        val baseLeftY = centerY
        val baseRightX = centerX + width / 2
        val baseRightY = centerY

        // Move to top vertex (end point of the triangle)
        path.moveTo(endX, endY)

        // Draw lines from top vertex to the base corners
        path.lineTo(baseRightX, baseRightY)
        path.lineTo(baseLeftX, baseLeftY)

        // Close the path to form a triangle
        path.close()

        return path
    }

    private fun renderMinorTicks(canvas: Canvas) {
        for (s in MIN_SPEED..maxPercent step 2) {
            if (s % 10 != 0) {
                canvas.drawLine(
                    centerX + (centerX - borderSize - MINOR_TICK_SIZE) * cos(mapSpeedToAngle(s).toRadian()),
                    centerY - (centerY - borderSize - MINOR_TICK_SIZE) * sin(mapSpeedToAngle(s).toRadian()),
                    centerX + (centerX - borderSize - TICK_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                    centerY - (centerY - borderSize - TICK_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                    paintMinorTick
                )
            }
        }
    }

    private fun renderMajorTicks(canvas: Canvas) {
        for (s in MIN_SPEED..maxPercent step 10) {
            canvas.drawLine(
                centerX + (centerX - borderSize - MAJOR_TICK_SIZE) * cos(mapSpeedToAngle(s).toRadian()),
                centerY - (centerY - borderSize - MAJOR_TICK_SIZE) * sin(mapSpeedToAngle(s).toRadian()),
                centerX + (centerX - borderSize - TICK_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                centerY - (centerY - borderSize - TICK_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                paintMajorTick
            )
            if (percentText) {
                canvas.drawTextCentred(
                    s.toString(),
                    centerX + (centerX - borderSize - MAJOR_TICK_SIZE - TICK_MARGIN - TICK_TEXT_MARGIN) * cos(mapSpeedToAngle(s).toRadian()),
                    centerY - (centerY - borderSize - MAJOR_TICK_SIZE - TICK_MARGIN - TICK_TEXT_MARGIN) * sin(mapSpeedToAngle(s).toRadian()),
                    paintTickText
                )
            }
        }
    }

    private fun renderBorder(canvas: Canvas) {
        canvas.drawArc(
            indicatorBorderRect,
            140f,
            260f,
            false,
            paintIndicatorBorder
        )
    }

    private fun renderTickBorder(canvas: Canvas) {
        canvas.drawArc(
            tickBorderRect,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            paintTickBorder
        )
    }

    private fun renderBorderFill(canvas: Canvas) {
        val sweepAngle = (firstPercent / maxPercent.toFloat()) * SWEEP_ANGLE
        if (mode == 0) {
            paintIndicatorFill.color = fillColor
        } else {
            paintIndicatorFill.color = when {
                firstPercent <= 25 -> _firstQuarterColor
                firstPercent in 26..50 -> _secondQuarterColor
                firstPercent in 51..75 -> _thirdQuarterColor
                else -> _fourthQuarterColor
            }
        }
        canvas.drawArc(
            indicatorBorderRect,
            START_ANGLE,
            sweepAngle,
            false,
            paintIndicatorFill
        )
    }

    private fun mapSpeedToAngle(speed: Int): Float {
        return (MIN_ANGLE + ((MAX_ANGLE - MIN_ANGLE) / (maxPercent - MIN_SPEED)) * (speed - MIN_SPEED))
    }

    private fun mapAngleToSpeed(angle: Float): Int {
        return (MIN_SPEED + ((maxPercent - MIN_SPEED) / (MAX_ANGLE - MIN_ANGLE)) * (angle - MIN_ANGLE)).toInt()
    }

    fun setPercent(s: Int, d: Long, onEnd: (() -> Unit)? = null) {
        val newPercent = s.coerceIn(MIN_SPEED, maxPercent)

        animator.apply {
            setFloatValues(mapSpeedToAngle(firstPercent), mapSpeedToAngle(newPercent))

            addUpdateListener {
                angle = it.animatedValue as Float
                firstPercent = mapAngleToSpeed(angle)
                invalidate()
            }

            doOnEnd {
                onEnd?.invoke()
            }

            duration = d
            start()
        }
    }

    private fun Canvas.drawTextCentred(text: String, cx: Float, cy: Float, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }

    private fun Float.toRadian(): Float {
        return this * (PI / 180).toFloat()
    }

    companion object {
        private const val MIN_ANGLE = 220f
        private const val MAX_ANGLE = -40f
        private const val START_ANGLE = 140f
        private const val SWEEP_ANGLE = 260f

        private const val MIN_SPEED = 0
        private const val TICK_MARGIN = 10f
        private const val TICK_TEXT_MARGIN = 0f
        private const val MAJOR_TICK_SIZE = 50f
        private const val MINOR_TICK_SIZE = 25f
        private const val MAJOR_TICK_WIDTH = 4f
        private const val MINOR_TICK_WIDTH = 2f
    }
}
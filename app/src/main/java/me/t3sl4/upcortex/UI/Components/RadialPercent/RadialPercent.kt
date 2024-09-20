package me.t3sl4.upcortex.UI.Components.RadialPercent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import me.t3sl4.upcortex.R

class RadialPercent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var baseColor: Int = Color.GRAY
    private var fillColor: Int = Color.BLUE
    private var currentPercent: Int = 0
    private var strokeWidth: Float = 20f
    private var radius: Float = 0f
    private var clockwise: Boolean = true
    private var textColor: Int = Color.BLACK
    private var textFont: Typeface? = null
    private var textSize: Float = 100f // Varsayılan text boyutu

    private val paintBase = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val capPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        // Retrieve XML attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RadialPercent,
            0, 0
        ).apply {
            try {
                baseColor = getColor(R.styleable.RadialPercent_baseColor, Color.GRAY)
                fillColor = getColor(R.styleable.RadialPercent_percentColor, Color.BLUE)
                currentPercent = getInt(R.styleable.RadialPercent_currentPercent, 0)
                strokeWidth = getDimension(R.styleable.RadialPercent_percentStrokeWidth, 20f)
                radius = getDimension(R.styleable.RadialPercent_percentRadius, (width / 2).toFloat())
                clockwise = getBoolean(R.styleable.RadialPercent_percentClockwise, true)
                textColor = getColor(R.styleable.RadialPercent_percentTextColor, Color.BLACK)
                textSize = getDimension(R.styleable.RadialPercent_percentTextSize, 100f) // Text boyutunu XML'den al
                val textFontId = getResourceId(R.styleable.RadialPercent_percentTextFont, -1)
                if (textFontId != -1) {
                    textFont = ResourcesCompat.getFont(context, textFontId)
                }
            } finally {
                recycle()
            }
        }

        // Initialize paints
        paintBase.color = baseColor
        paintBase.style = Paint.Style.STROKE
        paintBase.strokeWidth = strokeWidth

        paintFill.color = fillColor
        paintFill.style = Paint.Style.STROKE
        paintFill.strokeWidth = strokeWidth
        paintFill.strokeCap = Paint.Cap.ROUND

        paintText.color = textColor
        paintText.textSize = textSize
        paintText.textAlign = Paint.Align.CENTER
        paintText.typeface = textFont
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()

        // Base çemberi çiz
        canvas.drawCircle(centerX, centerY, (width / 2) - strokeWidth / 2, paintBase)

        // Boyalı arc'ı çiz
        val rectF = RectF(
            centerX - (width / 2) + strokeWidth / 2,
            centerY - (height / 2) + strokeWidth / 2,
            centerX + (width / 2) - strokeWidth / 2,
            centerY + (height / 2) - strokeWidth / 2
        )
        val sweepAngle = (360 * currentPercent / 100).toFloat() * if (clockwise) 1 else -1
        canvas.drawArc(rectF, -90f, sweepAngle, false, paintFill)

        // Ortadaki yüzdeyi çiz
        canvas.drawText("$currentPercent%", centerX, centerY + paintText.textSize / 3, paintText)
    }

    // Method to update the current percentage
    fun setCurrentPercent(percent: Int) {
        currentPercent = percent
        invalidate() // Redraw the view
    }
}
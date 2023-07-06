package com.nexlesoft.ket.ui.custom.passwordstrengthmeter

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.nexlesoft.ket.R

class StrengthIndicatorView(context: Context?) : View(context!!) {
    private var height: Int = 0
    private var securityLevel = -1
    private var animate = false
    private var animDuration = 0
    private val linePaint = Paint()
    private val backgroundPaint = Paint()
    private var levels: List<PasswordStrengthLevel> = emptyList()
    private var currentLineWidth = 0
    private var correctLineWidth = 0
    private var currentColor = 0
    private var correctColor = 0

    fun setPasswordStrengthLevels(levels: List<PasswordStrengthLevel>) {
        this.levels = levels
        backgroundPaint.color = resources.getColor(R.color.purple)
        currentColor = resources.getColor(levels[0].indicatorColor)
    }

    fun setAnimDuration(animDuration: Int) {
        this.animDuration = animDuration
    }

    fun setAnimate(animate: Boolean) {
        this.animate = animate
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, height)
    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        if (changed) setSecurityLevel(securityLevel)
//    }

    fun setSecurityLevel(level: Int) {
        if (securityLevel != level) {
            if (level >= 0) {
                backgroundPaint.color = resources.getColor(android.R.color.darker_gray)
            }
            securityLevel = level
            correctLineWidth =
                if (level + 1 == levels.size) width else measuredWidth / (levels.size - 1) * level
            correctColor = resources.getColor(levels[level].indicatorColor)
            refresh()
        }
    }

    fun resetIndicator() {
        backgroundPaint.color = resources.getColor(R.color.purple)
        currentColor = resources.getColor(levels[0].indicatorColor)
        securityLevel = -1
        currentLineWidth = 0
        correctLineWidth = 0
        currentColor = 0
        correctColor = 0
        invalidate()
    }

    private fun refresh() {
        val duration = if (animate) animDuration else 0
        val colorProp = PropertyValuesHolder.ofInt("color", currentColor, correctColor)
        val widthProperty = PropertyValuesHolder.ofInt("width", currentLineWidth, correctLineWidth)
        val colorAnim = ValueAnimator()
        colorAnim.duration = duration.toLong()
        colorAnim.setValues(colorProp, widthProperty)
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.interpolator = AccelerateDecelerateInterpolator()
        colorAnim.addUpdateListener { animation ->
            currentColor = animation.getAnimatedValue("color") as Int
            currentLineWidth = animation.getAnimatedValue("width") as Int
            invalidate()
        }
        colorAnim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        linePaint.strokeWidth = height.toFloat()
        linePaint.color = currentColor
        backgroundPaint.strokeWidth = height.toFloat()
        canvas.drawLine(
            0f,
            (height / 2).toFloat(),
            width.toFloat(),
            (height / 2).toFloat(),
            backgroundPaint
        )
        canvas.drawLine(
            0f,
            (height / 2).toFloat(),
            currentLineWidth.toFloat(),
            (height / 2).toFloat(),
            linePaint
        )
    }
}
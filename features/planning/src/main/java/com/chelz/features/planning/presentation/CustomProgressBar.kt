package com.chelz.features.planning.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar
import com.chelz.libraries.theme.getThemeColor
import com.google.android.material.R as MaterialR

class CustomProgressBar @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
) : ProgressBar(context, attrs, defStyleAttr) {

	val colorError = getThemeColor(context, MaterialR.attr.colorError)
	val colorPrimary = getThemeColor(context, MaterialR.attr.colorPrimary)
	val colorSurface = getThemeColor(context, MaterialR.attr.colorSurfaceVariant)
	val colorMedium = context.getColor(android.R.color.holo_orange_light)

	init {
		this.max = 100
	}

	var progressColor = colorPrimary
	private var progress: Float = 0f

	private val progressPaint = Paint().apply {
		style = Paint.Style.FILL
	}

	private val backgroundPaint = Paint().apply {
		color = colorSurface
		style = Paint.Style.FILL
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		val progressWidth = width * progress / 100f
		canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
		progressPaint.color = progressColor
		canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), progressPaint)
	}

	fun setProgressSmoothly(progress: Double, duration: Long = 500) {
		val animator = ValueAnimator.ofFloat(this.progress, progress.toFloat())
		progressColor = getProgressColor(progress.toFloat() / 100.0f)
		animator.addUpdateListener { animation ->
			this.progress = animation.animatedValue as Float
			invalidate()
		}
		animator.duration = duration
		animator.start()
	}

	private fun getProgressColor(progressFraction: Float): Int {
		val colorStops = floatArrayOf(0f, 0.5f, 1f)
		val colors = intArrayOf(colorPrimary, colorMedium, colorError)
		return interpolateColor(colors, colorStops, progressFraction)
	}

	private fun interpolateColor(colors: IntArray, colorStops: FloatArray, fraction: Float): Int {
		val colorPositions = colorStops.copyOf()
		val colorList = colors.toMutableList()
		colorPositions.sort()
		val stopIndex = colorPositions.indexOfFirst { it >= fraction }
		if (stopIndex == 0) {
			return colorList[0]
		}
		if (stopIndex == colorPositions.size || stopIndex == -1) {
			return colorList.last()
		}
		val startFraction = colorPositions[stopIndex - 1]
		val endFraction = colorPositions[stopIndex]
		val intervalFraction = (fraction - startFraction) / (endFraction - startFraction)
		val startColor = colorList[stopIndex - 1]
		val endColor = colorList[stopIndex]
		return ArgbEvaluator().evaluate(intervalFraction, startColor, endColor) as Int
	}
}
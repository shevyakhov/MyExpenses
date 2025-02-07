package com.chelz.libraries.theme

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources

fun getThemeDrawable(context: Context, @AttrRes attr: Int): Drawable? {
	val styledResId = getThemeResourceId(context, attr)
	return AppCompatResources.getDrawable(context, styledResId)
}

@AnyRes
fun getThemeResourceId(context: Context, @AttrRes attr: Int, @AnyRes defaultResourceId: Int = 0): Int {
	val styledAttrs = getThemeStyledAttributes(context, attr)
	val resourceId = styledAttrs.getResourceId(0, defaultResourceId)
	styledAttrs.recycle()
	return resourceId
}

@ColorInt
fun getThemeColor(context: Context, @AttrRes attr: Int, @ColorInt defaultColor: Int = 0): Int {
	val styledAttrs = getThemeStyledAttributes(context, attr)
	val color = styledAttrs.getColor(0, defaultColor)
	styledAttrs.recycle()
	return color
}

fun getThemeStyledAttributes(context: Context, @AttrRes attr: Int): TypedArray {
	val typedValue = TypedValue()
	context.theme.resolveAttribute(attr, typedValue, true)
	return context.obtainStyledAttributes(typedValue.resourceId, intArrayOf(attr))
}

class ColorGenerator {
	companion object {

		private val semanticColors = listOf(
			"#FF4267", // semantic_red
			"#0890FE", // semantic_blue
			"#FFAF2A", // semantic_yellow
			"#52D5BA", // semantic_cyan
			"#E0E0E0", // semantic_white
			"#FB6B18"  // semantic_orange
		)

		fun generateColors(size: Int, lightenFactor: Float = 0.2f, darkenFactor: Float = 0.1f): List<Int> {
			val modifiedColors = mutableListOf<Int>()
			for (i in 0 until size) {
				val semanticColor = semanticColors[i % semanticColors.size]
				val modifiedColor = modifyColor(Color.parseColor(semanticColor), lightenFactor, darkenFactor)
				modifiedColors.add(modifiedColor)
			}
			return modifiedColors
		}

		private fun modifyColor(color: Int, lightenFactor: Float, darkenFactor: Float): Int {
			val hsv = FloatArray(3)
			Color.colorToHSV(color, hsv)
			hsv[2] *= (1 + lightenFactor - darkenFactor)
			return Color.HSVToColor(hsv)
		}
	}
}

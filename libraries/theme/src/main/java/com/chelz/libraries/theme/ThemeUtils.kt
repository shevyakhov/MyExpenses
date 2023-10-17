package com.chelz.libraries.theme

import android.content.Context
import android.content.res.TypedArray
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
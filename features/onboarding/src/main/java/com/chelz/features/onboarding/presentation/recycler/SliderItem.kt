package com.chelz.features.onboarding.presentation.recycler

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.airbnb.lottie.LottieDrawable
import com.chelz.features.onboarding.R

data class SliderItem(
	@RawRes val animationRaw: Int,
	@StringRes val mainText: Int,
	@StringRes val subText: Int,
	val animationMode: Int = LottieDrawable.RESTART,
)

val introList = listOf(
	SliderItem(R.raw.expenses, R.string.introFirstMain, R.string.introFirstSub, LottieDrawable.REVERSE),
	SliderItem(R.raw.analysis, R.string.introSecondMain, R.string.introSecondSub),
	SliderItem(R.raw.account, R.string.introThirdMain, R.string.introThirdSub, LottieDrawable.INFINITE)
)
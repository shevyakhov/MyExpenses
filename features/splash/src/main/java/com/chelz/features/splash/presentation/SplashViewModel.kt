package com.chelz.features.splash.presentation

import androidx.lifecycle.ViewModel
import com.chelz.features.splash.presentation.navigation.SplashRouter

class SplashViewModel(val firstStartFlag: Boolean, private val router: SplashRouter) : ViewModel() {

	fun init() {
		if (firstStartFlag) {
			router.goToOnBoarding()
		} else
			router.goToMain()
	}

}
package com.chelz.features.splash.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.chelz.features.splash.presentation.navigation.SplashRouter

class SplashViewModel(val firstStartFlag: Boolean, private val router: SplashRouter) : ViewModel() {

	private fun isUserLogged(): Boolean {
		return false
	}

	fun init() {
		/*if (!isUserLogged()) {
			router.goToOnBoarding()
		} else
			router.goToMain()*/
	}

}
package com.chelz.myexpenses.presentation

import androidx.lifecycle.ViewModel
import com.chelz.features.splash.SplashDestination
import com.chelz.libraries.navigation.GlobalRouter

class MainViewModel(
	private val router: GlobalRouter,
) : ViewModel() {

	private var mainRootOpened = false

	fun openMainRoot(firstStart: Boolean) {
		if (!mainRootOpened) {
			router.newRoot(SplashDestination(firstStart))
		}
		mainRootOpened = true
	}
}
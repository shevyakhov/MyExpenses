package com.chelz.myexpenses.di

import com.chelz.features.main.MainFragmentDestination
import com.chelz.features.splash.presentation.navigation.SplashRouter
import com.chelz.libraries.navigation.GlobalRouter

class SplashRouterImpl(private val router: GlobalRouter) : SplashRouter {

	override fun goToOnBoarding() {
		//router.replace(OnBoardingDestination)
		router.replace(MainFragmentDestination)

	}

	override fun goToMain() {
		router.replace(MainFragmentDestination)
	}
}
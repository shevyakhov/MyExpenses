package com.chelz.myexpenses.di

import com.chelz.features.onboarding.presentation.navigation.OnBoardingRouter
import com.chelz.libraries.navigation.GlobalRouter

class OnBoardingRouterImpl(private val router: GlobalRouter) : OnBoardingRouter {

	override fun navigateToRegistration() {
		//router.replace(RegistrationDestination)
	}
}

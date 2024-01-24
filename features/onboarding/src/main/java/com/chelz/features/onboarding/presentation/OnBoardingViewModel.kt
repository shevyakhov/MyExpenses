package com.chelz.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.chelz.features.onboarding.presentation.navigation.OnBoardingRouter

class OnBoardingViewModel(val router: OnBoardingRouter) : ViewModel() {

	fun navigateToRegistration() {
		router.navigateToRegistration()
	}
}
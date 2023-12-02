package com.chelz.myexpenses.di

import com.chelz.features.registration.presentation.navigation.RegistrationRouter
import com.chelz.libraries.navigation.GlobalRouter
import com.chelz.login.LoginDestination

class RegistrationRouterImpl(private val router: GlobalRouter) : RegistrationRouter {

	override fun navigateToLogin() {
		router.open(LoginDestination)
	}
}

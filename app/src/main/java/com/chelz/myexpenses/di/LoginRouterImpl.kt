package com.chelz.myexpenses.di

import com.chelz.features.main.MainFragmentDestination
import com.chelz.libraries.navigation.GlobalRouter
import com.chelz.login.presentation.navigation.LoginRouter

class LoginRouterImpl(private val router: GlobalRouter) : LoginRouter {

	override fun navigateToRegistration() {
		router.exit()
	}

	override fun navigateToHomeScreen() {
		router.newRoot(MainFragmentDestination)
	}
}

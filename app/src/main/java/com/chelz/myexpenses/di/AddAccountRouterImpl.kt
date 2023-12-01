package com.chelz.myexpenses.di

import com.chelz.features.accountadd.presentation.navigation.AddAccountRouter
import com.chelz.libraries.navigation.GlobalRouter

class AddAccountRouterImpl(private val router: GlobalRouter) : AddAccountRouter {

	override fun navigateBack() {
		router.exit()
	}
}

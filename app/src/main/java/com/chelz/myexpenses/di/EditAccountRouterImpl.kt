package com.chelz.myexpenses.di

import com.chelz.features.accountedit.presentation.navigation.EditAccountRouter
import com.chelz.libraries.navigation.GlobalRouter

class EditAccountRouterImpl(private val router: GlobalRouter) : EditAccountRouter {

	override fun navigateBack() {
		router.exit()
	}
}

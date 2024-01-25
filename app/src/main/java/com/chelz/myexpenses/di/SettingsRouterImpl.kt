package com.chelz.myexpenses.di

import com.chelz.features.settings.presentation.navigation.SettingsRouter
import com.chelz.libraries.navigation.GlobalRouter

class SettingsRouterImpl(private val router: GlobalRouter) : SettingsRouter {

	override fun navigateBack() {
		router.exit()
	}
}

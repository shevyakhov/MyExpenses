package com.chelz.myexpenses.di

import com.chelz.features.categoryadd.presentation.navigation.AddCategoryRouter
import com.chelz.libraries.navigation.GlobalRouter

class AddCategoryRouterImpl(private val router: GlobalRouter) : AddCategoryRouter {

	override fun navigateBack() {
		router.exit()
	}
}

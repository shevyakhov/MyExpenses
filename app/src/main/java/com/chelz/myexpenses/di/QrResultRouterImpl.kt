package com.chelz.myexpenses.di

import com.chelz.features.qrscanner.presentation.result.navigation.QrResultRouter
import com.chelz.libraries.navigation.GlobalRouter

class QrResultRouterImpl(private val router: GlobalRouter) : QrResultRouter {

	override fun navigateBack() {
		router.exit()
	}

	override fun navigateToHome() {
		router.popToRoot()
	}
}
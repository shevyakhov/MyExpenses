package com.chelz.myexpenses.di

import com.chelz.features.qrscanner.presentation.navigation.QrScannerRouter
import com.chelz.libraries.navigation.GlobalRouter

class QrScannerRouterImpl(private val router: GlobalRouter) : QrScannerRouter {

	override fun navigateBack() {
		router.exit()
	}
}

package com.chelz.myexpenses.di

import com.chelz.features.accountadd.AddAccountDestination
import com.chelz.features.categoryadd.AddCategoryDestination
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.features.qrscanner.QrScannerDestination
import com.chelz.libraries.navigation.GlobalRouter

class HomeRouterImpl(private val router: GlobalRouter) : HomeRouter {

	override fun navigateToAddAccount() {
		router.open(AddAccountDestination)
	}

	override fun navigateToAddCategory() {
		router.open(AddCategoryDestination)
	}

	override fun navigateToQrScanner() {
		router.open(QrScannerDestination)
	}
}
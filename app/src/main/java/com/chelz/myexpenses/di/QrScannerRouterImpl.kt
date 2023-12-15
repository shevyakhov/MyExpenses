package com.chelz.myexpenses.di

import com.chelz.features.qrscanner.QrResultDestination
import com.chelz.features.qrscanner.domain.entity.HtmlEntity
import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.features.qrscanner.presentation.scanner.navigation.QrScannerRouter
import com.chelz.libraries.navigation.GlobalRouter

class QrScannerRouterImpl(private val router: GlobalRouter) : QrScannerRouter {

	override fun navigateBack() {
		router.exit()
	}

	override fun navigateToQrResults(items: ItemsEntity, htmlEntity: HtmlEntity) {
		router.open(QrResultDestination(items, htmlEntity))
	}
}

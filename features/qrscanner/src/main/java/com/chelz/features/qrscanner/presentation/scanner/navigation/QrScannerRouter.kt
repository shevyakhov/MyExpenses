package com.chelz.features.qrscanner.presentation.scanner.navigation

import com.chelz.features.qrscanner.domain.entity.HtmlEntity
import com.chelz.features.qrscanner.domain.entity.ItemsEntity

interface QrScannerRouter {

	fun navigateBack()
	fun navigateToQrResults(items: ItemsEntity, htmlEntity: HtmlEntity)
}

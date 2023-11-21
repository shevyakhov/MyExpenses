package com.chelz.features.qrscanner.presentation

import androidx.lifecycle.ViewModel
import com.chelz.features.qrscanner.presentation.navigation.QrScannerRouter

class QrScannerViewModel(
	private val router: QrScannerRouter,
) : ViewModel() {

	fun onQrScanned(result: String) {

	}
}
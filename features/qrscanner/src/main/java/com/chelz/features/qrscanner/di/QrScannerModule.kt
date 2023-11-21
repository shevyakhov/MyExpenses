package com.chelz.features.qrscanner.di

import com.chelz.features.qrscanner.presentation.QrScannerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val QrScannerModule = module {
	viewModel {
		QrScannerViewModel(
			router = get()
		)
	}
}
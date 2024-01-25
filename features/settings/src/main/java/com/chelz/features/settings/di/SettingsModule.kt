package com.chelz.features.settings.di

import com.chelz.features.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SettingsModule = module {
	viewModel {
		SettingsViewModel(
			router = get(),
			application = androidApplication()
		)
	}
}
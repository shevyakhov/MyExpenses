package com.chelz.features.splash.di

import com.chelz.features.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SplashModule = module {
	viewModel { (firstStartFlag: Boolean) ->
		SplashViewModel(
			firstStartFlag = firstStartFlag,
			router = get()
		)
	}
}
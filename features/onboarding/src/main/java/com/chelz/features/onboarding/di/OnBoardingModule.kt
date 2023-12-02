package com.chelz.features.onboarding.di

import com.chelz.features.onboarding.presentation.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val OnBoardingModule = module {
	viewModel {
		OnBoardingViewModel(
			router = get()
		)
	}
}
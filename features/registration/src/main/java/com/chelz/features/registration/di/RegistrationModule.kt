package com.chelz.features.registration.di

import com.chelz.features.registration.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RegistrationModule = module {
	viewModel {
		RegistrationViewModel(
			router = get(),
			insertUserUseCase = get()
		)
	}
}
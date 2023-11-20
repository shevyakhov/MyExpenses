package com.chelz.features.accountadd.di

import com.chelz.features.accountadd.presentation.AddAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AddAccountModule = module {

	viewModel {
		AddAccountViewModel(
			router = get(),
			insertAccountsUseCase = get()
		)
	}
}
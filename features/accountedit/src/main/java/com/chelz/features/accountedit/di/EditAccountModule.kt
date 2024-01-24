package com.chelz.features.accountedit.di

import com.chelz.features.accountedit.presentation.EditAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val EditAccountModule = module {
	viewModel {
		EditAccountViewModel(
			router = get(),
			deleteAccountUseCase = get(),
			updateAccountUseCase = get(),
		)
	}
}
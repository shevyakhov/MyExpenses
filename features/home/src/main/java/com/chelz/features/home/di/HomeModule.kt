package com.chelz.features.home.di

import com.chelz.features.home.domain.usecase.GetTodaySpendScenario
import com.chelz.features.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val HomeModule = module {
	single { GetTodaySpendScenario() }

	viewModel {
		HomeViewModel(
			router = get(),
			getAllAccountsUseCase = get(),
			getAllCategoriesUseCase = get(),
			getAllOperationsUseCase = get(),
			insertOperationUseCase = get(),
			updateAccountUseCase = get(),
			getAccountByIdUseCase = get(),
			getCategoryByIdUseCase = get(),
			getTodaySpendScenario = get()
		)
	}
}
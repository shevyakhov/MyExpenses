package com.chelz.features.statistics.di

import com.chelz.features.statistics.presentation.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val StatisticsModule = module {
	viewModel {
		StatisticsViewModel(
			router = get(),
			getAllAccountsUseCase = get(),
			getAllCategoriesUseCase = get(),
			getAllOperationsUseCase = get(),
			getAccountByIdUseCase = get(),
			getCategoryByIdUseCase = get(),
		)
	}
}
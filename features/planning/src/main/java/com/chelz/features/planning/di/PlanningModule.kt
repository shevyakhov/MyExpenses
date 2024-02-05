package com.chelz.features.planning.di

import com.chelz.features.planning.presentation.PlanningViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlanningModule = module {
	viewModel {
		PlanningViewModel(
			getAllAccountsUseCase = get(),
			getAllCategoriesUseCase = get(),
			getAllOperationsUseCase = get(),
			getAccountByIdUseCase = get(),
			getCategoryByIdUseCase = get(),
			deleteCategoryUseCase = get(),
			clearDataBaseUseCase = get(),
			insertMonthGoalUseCase = get(),
			getMonthGoalByIdUseCase = get(),
			getMonthGoalByAccountAndCategoryUseCase = get(),
			getAllMonthGoalsUseCase = get(),
			updateMonthGoalUseCase = get(),
			deleteMonthGoalByIdUseCase = get(),
			router = get()
		)
	}
}
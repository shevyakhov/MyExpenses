package com.chelz.features.profile.di

import com.chelz.features.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ProfileModule = module {
	viewModel {
		ProfileViewModel(
			router = get(),
			getAllAccountsUseCase = get(),
			getAllCategoriesUseCase = get(),
			getAllOperationsUseCase = get(),
			getAccountByIdUseCase = get(),
			getCategoryByIdUseCase = get(),
			deleteCategoryUseCase = get(),
			clearDataBaseUseCase = get(),
		)
	}
}
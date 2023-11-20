package com.chelz.features.categoryadd.di

import com.chelz.features.categoryadd.presentation.AddCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AddCategoryFragmentModule = module {
	viewModel {
		AddCategoryViewModel(
			router = get(),
			insertCategoryUseCase = get()
		)
	}

}
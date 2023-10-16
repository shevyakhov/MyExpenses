package com.chelz.features.main.di

import com.chelz.features.main.presentation.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainFragmentModule = module {
	viewModel {
		MainFragmentViewModel(get())
	}
}
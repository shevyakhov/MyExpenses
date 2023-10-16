package com.chelz.features.planning.di

import com.chelz.features.planning.presentation.PlanningViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlanningModule = module {
	viewModel { PlanningViewModel(router = get()) }
}
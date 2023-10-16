package com.chelz.features.profile.di

import com.chelz.features.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ProfileModule = module {
	viewModel { ProfileViewModel(router = get()) }
}
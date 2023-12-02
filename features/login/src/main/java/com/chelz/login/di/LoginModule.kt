package com.chelz.login.di

import com.chelz.login.domain.usecase.InsertFirstStartUseCase
import com.chelz.login.presentation.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val LoginModule = module {
	factory { InsertFirstStartUseCase(androidContext()) }

	viewModel {
		LoginViewModel(
			router = get(),
			insertFirstStartUseCase = get()
		)
	}
}
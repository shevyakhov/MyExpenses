package com.chelz.features.profile.di

import com.chelz.features.profile.data.api.NewsApi
import com.chelz.features.profile.data.datasource.NewsDataSource
import com.chelz.features.profile.data.datasource.NewsDataSourceImpl
import com.chelz.features.profile.data.repository.NewsRepositoryImpl
import com.chelz.features.profile.domain.repository.NewsRepository
import com.chelz.features.profile.domain.usecase.NewsUseCase
import com.chelz.features.profile.presentation.ProfileViewModel
import com.chelz.network.RETROFIT_NEWS
import com.chelz.network.service.createRetrofitService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ProfileModule = module {
	factory { createRetrofitService<NewsApi>(get(named(RETROFIT_NEWS))) }

	single<NewsDataSource> { NewsDataSourceImpl(get()) }

	single<NewsRepository> {
		NewsRepositoryImpl(get())
	}
	factory { NewsUseCase(get()) }

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
			newsUseCase = get()
		)
	}
}
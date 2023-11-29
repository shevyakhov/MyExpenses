package com.chelz.features.qrscanner.di

import com.chelz.features.qrscanner.data.api.QrApi
import com.chelz.features.qrscanner.data.datasource.QrDataSource
import com.chelz.features.qrscanner.data.datasource.QrDataSourceImpl
import com.chelz.features.qrscanner.data.datasource.QrTokenDataSource
import com.chelz.features.qrscanner.data.datasource.QrTokenDataSourceImpl
import com.chelz.features.qrscanner.data.repository.QrRepositoryImpl
import com.chelz.features.qrscanner.data.repository.QrTokenRepositoryImpl
import com.chelz.features.qrscanner.domain.repository.QrRepository
import com.chelz.features.qrscanner.domain.repository.QrTokenRepository
import com.chelz.features.qrscanner.domain.usecase.GetQrResultUseCase
import com.chelz.features.qrscanner.domain.usecase.GetTokenUseCase
import com.chelz.features.qrscanner.presentation.result.QrResultViewModel
import com.chelz.features.qrscanner.presentation.scanner.QrScannerViewModel
import com.chelz.network.BACKEND_QR_KEY
import com.chelz.network.RETROFIT_QR
import com.chelz.network.service.createRetrofitService
import com.chelz.network.service.getRetrofit
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val QrScannerModule = module {

	factory { createRetrofitService<QrApi>(get(named(RETROFIT_QR))) }

	single<QrDataSource> { QrDataSourceImpl(get()) }
	single<QrTokenDataSource> { QrTokenDataSourceImpl(getProperty(BACKEND_QR_KEY)) }

	single<QrRepository> {
		QrRepositoryImpl(get())
	}
	single<QrTokenRepository> {
		QrTokenRepositoryImpl(get())
	}
	factory { GetQrResultUseCase(get()) }
	factory { GetTokenUseCase(get()) }
	single(named(RETROFIT_QR)) { createRetrofitService<QrApi>(getRetrofit()) }

	viewModel {
		QrScannerViewModel(
			router = get(),
			getQrResultUseCase = get(),
			getTokenUseCase = get()
		)
	}
}

internal val QrResultModule = module {
	viewModel {
		QrResultViewModel(
			router = get(),
			getQrResultUseCase = get(),
			getTokenUseCase = get(),
			getAllAccountsUseCase = get(),
			getAllCategoriesUseCase = get()
		)
	}
}
val QrModule = listOf(QrScannerModule, QrResultModule)
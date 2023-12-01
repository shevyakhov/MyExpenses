package com.chelz.network

import com.chelz.network.providers.provideOkHttpClient
import com.chelz.network.providers.provideQrOkHttpClient
import com.chelz.network.providers.provideRetrofit
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BACKEND_QR = "BACKEND"
const val BASIC_CLIENT = "BasicClient"
const val QR_CLIENT = "QrClient"
const val RETROFIT_BASIC = "RETROFIT_BASIC"
const val RETROFIT_QR = "RETROFIT_QR"
const val BACKEND_QR_KEY = "BACKEND_QR_KEY"
val NetworkModule = module {
	single(named(BASIC_CLIENT)) {
		provideOkHttpClient()
	}
	single(named(QR_CLIENT)) {
		provideQrOkHttpClient(getProperty(BACKEND_QR_KEY))
	}
	single(named(RETROFIT_BASIC)) {
		provideRetrofit(
			okHttpClient = get(named(BASIC_CLIENT)),
			url = getProperty(BACKEND_QR)
		)
	}
	single(named(RETROFIT_QR)) {
		provideRetrofit(
			okHttpClient = get(named(QR_CLIENT)),
			url = getProperty(BACKEND_QR)
		)
	}
}
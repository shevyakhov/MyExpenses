package com.chelz.network

import com.chelz.network.providers.provideOkHttpClient
import com.chelz.network.providers.provideRetrofit
import org.koin.dsl.module

const val BACKEND = "BACKEND"
val NetworkModule = module {
	single {
		provideOkHttpClient()
	}
	single {
		provideRetrofit(
			okHttpClient = get(),
			url = getProperty(BACKEND)
		)
	}
}
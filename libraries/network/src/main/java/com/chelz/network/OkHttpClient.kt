package com.chelz.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val VALUE_TIMEOUT: Long = 60

internal fun okHttpClientSetups(): OkHttpClient =
	OkHttpClient().newBuilder()
		.applyDefaultSetups()
		.build()

private fun OkHttpClient.Builder.applyDefaultSetups(): OkHttpClient.Builder {
	connectTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
	writeTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)
	readTimeout(VALUE_TIMEOUT, TimeUnit.SECONDS)

	hostnameVerifier { _, _ -> true }
	return this
}
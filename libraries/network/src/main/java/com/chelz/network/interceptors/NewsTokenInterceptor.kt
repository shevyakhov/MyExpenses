package com.chelz.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class NewsTokenInterceptor(private val apiKey: String) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()

		val requestWithApiKey = originalRequest.newBuilder()
			.header("X-Api-Key", apiKey)
			.build()

		return chain.proceed(requestWithApiKey)
	}
}
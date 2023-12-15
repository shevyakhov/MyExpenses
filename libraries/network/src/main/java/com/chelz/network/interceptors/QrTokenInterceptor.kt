package com.chelz.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class QrTokenInterceptor : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val original = chain.request()
		val request = original.newBuilder()
			.header("Cookie", "ENGID=1.1")
			.method(original.method, original.body)
			.build()

		return chain.proceed(request)
	}
}
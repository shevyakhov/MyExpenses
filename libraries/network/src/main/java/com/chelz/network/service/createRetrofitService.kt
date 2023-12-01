package com.chelz.network.service

import org.koin.core.scope.Scope
import retrofit2.Retrofit

/**
 * You have to use getRetrofit() to get [retrofit]
 * */
inline fun <reified T> createRetrofitService(retrofit: Retrofit): T =
	retrofit.create(T::class.java)

inline fun <reified T> Scope.getRetrofit(): T = get(T::class)
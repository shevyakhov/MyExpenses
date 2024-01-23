package com.chelz.network.providers

import com.chelz.network.okHttpClientSetups
import com.chelz.network.okHttpNewsClientSetups
import com.chelz.network.okHttpQrClientSetups
import okhttp3.OkHttpClient

fun provideOkHttpClient(): OkHttpClient = okHttpClientSetups()
fun provideQrOkHttpClient(key: String): OkHttpClient = okHttpQrClientSetups()
fun provideNewsOkHttpClient(key: String): OkHttpClient = okHttpNewsClientSetups(key)
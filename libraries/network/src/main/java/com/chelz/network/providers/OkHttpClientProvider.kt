package com.chelz.network.providers

import com.chelz.network.okHttpClientSetups
import okhttp3.OkHttpClient

fun provideOkHttpClient(): OkHttpClient = okHttpClientSetups()
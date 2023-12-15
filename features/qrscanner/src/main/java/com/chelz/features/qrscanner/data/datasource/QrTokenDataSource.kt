package com.chelz.features.qrscanner.data.datasource

interface QrTokenDataSource {

	suspend fun getToken(): String
}
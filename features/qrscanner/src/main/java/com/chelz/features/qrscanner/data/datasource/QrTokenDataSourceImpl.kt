package com.chelz.features.qrscanner.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrTokenDataSourceImpl(private val key: String) : QrTokenDataSource {

	override suspend fun getToken(): String =
		withContext(Dispatchers.IO) {
			key
		}
}
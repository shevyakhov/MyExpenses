package com.chelz.features.qrscanner.data.datasource

import com.chelz.features.qrscanner.data.api.QrApi
import com.chelz.features.qrscanner.data.dto.ReceiptResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QrDataSourceImpl(private val api: QrApi) : QrDataSource {

	override suspend fun getQrResult(raw: String, token: String): ReceiptResult =
		withContext(Dispatchers.IO) {
			api.getQrResult(qrraw = raw, token = token)
		}
}
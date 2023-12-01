package com.chelz.features.qrscanner.data.repository

import com.chelz.features.qrscanner.data.datasource.QrDataSource
import com.chelz.features.qrscanner.data.dto.ReceiptResult
import com.chelz.features.qrscanner.domain.repository.QrRepository

class QrRepositoryImpl(private val dataSource: QrDataSource) : QrRepository {

	override suspend fun getQrResult(raw: String, token: String): ReceiptResult {
		return dataSource.getQrResult(raw, token = token)
	}
}



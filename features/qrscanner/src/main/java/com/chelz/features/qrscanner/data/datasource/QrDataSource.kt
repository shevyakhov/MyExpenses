package com.chelz.features.qrscanner.data.datasource

import com.chelz.features.qrscanner.data.dto.ReceiptResult

interface QrDataSource {

	suspend fun getQrResult(raw: String, token: String): ReceiptResult
}
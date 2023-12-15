package com.chelz.features.qrscanner.domain.repository

import com.chelz.features.qrscanner.data.dto.ReceiptResult

interface QrRepository {

	suspend fun getQrResult(raw: String, token: String): ReceiptResult
}
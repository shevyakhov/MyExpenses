package com.chelz.features.qrscanner.domain.usecase

import com.chelz.features.qrscanner.data.dto.ReceiptResult
import com.chelz.features.qrscanner.domain.repository.QrRepository

class GetQrResultUseCase(private val repository: QrRepository) {

	suspend operator fun invoke(raw: String, token: String): ReceiptResult {
		return repository.getQrResult(raw, token)
	}
}
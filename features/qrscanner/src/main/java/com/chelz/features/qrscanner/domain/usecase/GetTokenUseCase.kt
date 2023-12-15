package com.chelz.features.qrscanner.domain.usecase

import com.chelz.features.qrscanner.domain.repository.QrTokenRepository

class GetTokenUseCase(private val repository: QrTokenRepository) {

	suspend operator fun invoke(): String {
		return repository.getToken()
	}
}
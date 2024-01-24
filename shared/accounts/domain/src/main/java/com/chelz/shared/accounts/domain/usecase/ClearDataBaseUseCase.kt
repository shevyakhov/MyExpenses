package com.chelz.shared.accounts.domain.usecase

import com.chelz.shared.accounts.domain.repository.AccountRepository

class ClearDataBaseUseCase(private val repository: AccountRepository) {

	suspend operator fun invoke() {
		repository.clearDatabase()
	}
}
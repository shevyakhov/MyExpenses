package com.chelz.shared.accounts.domain.usecase.operation

import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.repository.AccountRepository

class InsertOperationUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(operation: Operation): Long {
		return accountRepository.insertOperation(operation)
	}
}
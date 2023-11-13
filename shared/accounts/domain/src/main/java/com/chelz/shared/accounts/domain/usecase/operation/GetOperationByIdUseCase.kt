package com.chelz.shared.accounts.domain.usecase.operation

import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetOperationByIdUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(id: Long): Operation {
		return accountRepository.getOperationById(id)
	}
}
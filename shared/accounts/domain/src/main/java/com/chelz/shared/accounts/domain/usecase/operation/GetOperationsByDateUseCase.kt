package com.chelz.shared.accounts.domain.usecase.operation

import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetOperationsByDateUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(date: Long): List<Operation> {
		return accountRepository.getOperationsByDate(date)
	}
}
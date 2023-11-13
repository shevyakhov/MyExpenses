package com.chelz.shared.accounts.domain.usecase.operation

import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetOperationsByCategoryUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(categoryId: Long): List<Operation> {
		return accountRepository.getOperationsByCategory(categoryId)
	}
}
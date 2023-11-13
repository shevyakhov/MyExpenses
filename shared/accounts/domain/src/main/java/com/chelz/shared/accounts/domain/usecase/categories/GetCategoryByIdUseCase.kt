package com.chelz.shared.accounts.domain.usecase.categories

import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetCategoryByIdUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(id: Long): Category {
		return accountRepository.getCategoryById(id)
	}
}
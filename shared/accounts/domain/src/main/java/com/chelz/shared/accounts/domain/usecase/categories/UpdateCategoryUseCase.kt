package com.chelz.shared.accounts.domain.usecase.categories

import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.repository.AccountRepository

class UpdateCategoryUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(category: Category) {
		accountRepository.updateCategory(category)
	}
}
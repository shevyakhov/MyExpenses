package com.chelz.shared.accounts.domain.usecase.categories

import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetAllCategoriesUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(): List<Category> {
		return accountRepository.getAllCategories()
	}
}
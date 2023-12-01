package com.chelz.shared.accounts.domain.usecase.user

import com.chelz.shared.accounts.domain.entity.User
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetUserByIdUseCase(private val repository: AccountRepository) {

	suspend operator fun invoke(id: Long): User? {
		return repository.getUserById(id)
	}
}
package com.chelz.shared.accounts.domain.usecase.user

import com.chelz.shared.accounts.domain.entity.User
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetAllUsersUseCase(private val repository: AccountRepository) {

	suspend operator fun invoke(): List<User> {
		return repository.getAllUsers()
	}
}
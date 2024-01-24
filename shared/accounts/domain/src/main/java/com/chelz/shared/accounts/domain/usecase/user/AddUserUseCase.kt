package com.chelz.shared.accounts.domain.usecase.user

import com.chelz.shared.accounts.domain.entity.User
import com.chelz.shared.accounts.domain.repository.AccountRepository

class AddUserUseCase(private val repository: AccountRepository) {

	suspend operator fun invoke(user: User) {
		repository.addUser(user)
	}
}
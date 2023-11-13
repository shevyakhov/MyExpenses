package com.chelz.shared.accounts.domain.usecase.account

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetAccountByIdUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(id: Long): Account {
		return accountRepository.getAccountById(id)
	}
}
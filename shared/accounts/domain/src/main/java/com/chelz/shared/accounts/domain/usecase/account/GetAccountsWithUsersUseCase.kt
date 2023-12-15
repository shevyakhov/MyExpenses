package com.chelz.shared.accounts.domain.usecase.account

import com.chelz.shared.accounts.domain.entity.AccountWithUsers
import com.chelz.shared.accounts.domain.repository.AccountRepository

class GetAccountsWithUsersUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(): List<AccountWithUsers> {
		return accountRepository.getAccountWithUsers()
	}
}
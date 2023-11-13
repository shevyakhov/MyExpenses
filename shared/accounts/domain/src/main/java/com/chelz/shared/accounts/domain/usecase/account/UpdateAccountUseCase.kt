package com.chelz.shared.accounts.domain.usecase.account

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.repository.AccountRepository

class UpdateAccountUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(account: Account) {
		accountRepository.updateAccount(account)
	}
}
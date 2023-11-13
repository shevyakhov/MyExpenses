package com.chelz.shared.accounts.domain.usecase.account

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.repository.AccountRepository

class DeleteAccountUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(account: Account) {
		accountRepository.deleteAccount(account)
	}
}
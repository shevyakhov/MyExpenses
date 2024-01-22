package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.AccountWithUsersDto

interface AccountDataSource {

	suspend fun getAllAccounts(): List<AccountDto>
	suspend fun getAccountById(id: Long): AccountDto
	suspend fun insertAccount(accountDto: AccountDto): Long
	suspend fun updateAccount(accountDto: AccountDto)
	suspend fun deleteAccount(accountDto: AccountDto)

	suspend fun getAccountsWithUsers(): List<AccountWithUsersDto>
	suspend fun clearAccounts()
}
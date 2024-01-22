package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dao.AccountDao
import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.AccountWithUsersDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountDataSourceImpl(private val accountDao: AccountDao) : AccountDataSource {

	override suspend fun getAllAccounts(): List<AccountDto> = withContext(Dispatchers.IO) {
		accountDao.getAllAccounts()
	}

	override suspend fun getAccountById(id: Long): AccountDto = withContext(Dispatchers.IO) {
		accountDao.getAccountById(id)
	}

	override suspend fun insertAccount(accountDto: AccountDto): Long = withContext(Dispatchers.IO) {
		accountDao.insertAccount(accountDto)
	}

	override suspend fun updateAccount(accountDto: AccountDto) = withContext(Dispatchers.IO) {
		accountDao.updateAccount(accountDto)
	}

	override suspend fun deleteAccount(accountDto: AccountDto) = withContext(Dispatchers.IO) {
		accountDao.deleteAccount(accountDto)
	}

	override suspend fun getAccountsWithUsers(): List<AccountWithUsersDto> = withContext(Dispatchers.IO) {
		accountDao.getAccountsWithUsers()
	}

	override suspend fun clearAccounts() = withContext(Dispatchers.IO) {
		accountDao.clearAccounts()
	}
}
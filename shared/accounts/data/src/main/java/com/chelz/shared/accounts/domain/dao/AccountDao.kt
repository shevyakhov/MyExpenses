package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.AccountWithUsersDto

@Dao
interface AccountDao {

	@Query("SELECT * FROM Accounts")
	suspend fun getAllAccounts(): List<AccountDto>

	@Query("SELECT * FROM Accounts WHERE accountId = :id")
	suspend fun getAccountById(id: Long): AccountDto

	@Insert
	suspend fun insertAccount(accountDto: AccountDto): Long

	@Update
	suspend fun updateAccount(accountDto: AccountDto)

	@Delete
	suspend fun deleteAccount(accountDto: AccountDto)

	@Transaction
	@Query("SELECT * FROM Accounts")
	fun getAccountsWithUsers(): List<AccountWithUsersDto>

	@Transaction
	@Query("DELETE FROM Accounts")
	fun clearAccounts()
}
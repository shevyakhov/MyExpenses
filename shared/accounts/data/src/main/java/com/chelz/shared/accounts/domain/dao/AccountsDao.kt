package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Query
import com.chelz.shared.accounts.domain.dto.AccountDto

@Dao
interface AccountsDao {

	@Query("SELECT * FROM Accounts")
	fun getAllAccounts(): AccountDto
}
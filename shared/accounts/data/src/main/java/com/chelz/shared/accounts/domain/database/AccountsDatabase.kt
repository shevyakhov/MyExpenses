package com.chelz.shared.accounts.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chelz.shared.accounts.domain.dao.AccountDao
import com.chelz.shared.accounts.domain.dao.CategoryDao
import com.chelz.shared.accounts.domain.dao.OperationDao
import com.chelz.shared.accounts.domain.dao.UserDao
import com.chelz.shared.accounts.domain.dto.AccountDto
import com.chelz.shared.accounts.domain.dto.AccountWithUsersJunctionDto
import com.chelz.shared.accounts.domain.dto.CategoryDto
import com.chelz.shared.accounts.domain.dto.OperationDto
import com.chelz.shared.accounts.domain.dto.UserDto

@Database(
	entities = [
		AccountDto::class,
		OperationDto::class,
		CategoryDto::class,
		UserDto::class,
		AccountWithUsersJunctionDto::class,
	], version = 1, exportSchema = false
)
abstract class AccountsDatabase : RoomDatabase() {

	companion object {

		const val NAME = "ACCOUNTS_DATA_BASE_NAME"
	}

	abstract fun accountsDao(): AccountDao
	abstract fun operationDao(): OperationDao
	abstract fun categoryDao(): CategoryDao
	abstract fun userDao(): UserDao
}
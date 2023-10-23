package com.chelz.shared.accounts.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
	entities = [
		AccountDto::class.java,
		OperationDto::class.java,
		CategoryDto::class.java], version = 1, exportSchema = false
)
abstract class AccountsDatabase : RoomDatabase() {

	companion object {

		const val NAME = "ACCOUNTS_DATA_BASE_NAME"
	}

	abstract fun userEscortsDao(): AccountsDao
}
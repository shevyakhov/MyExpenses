package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Accounts")
data class AccountDto(
	@PrimaryKey(autoGenerate = true) val accountId: Long,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "number") val number: String = "",
	@ColumnInfo(name = "color") val color: String = "#FF0000",
	@ColumnInfo(name = "money") val money: Double,
)
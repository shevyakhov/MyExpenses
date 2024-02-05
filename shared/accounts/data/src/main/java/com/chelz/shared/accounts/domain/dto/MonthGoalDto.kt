package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
	tableName = "MonthGoals",
	foreignKeys = [
		ForeignKey(entity = AccountDto::class, parentColumns = ["accountId"], childColumns = ["account"], onDelete = CASCADE),
		ForeignKey(entity = CategoryDto::class, parentColumns = ["categoryId"], childColumns = ["category"], onDelete = CASCADE)
	]
)
data class MonthGoalDto(
	@PrimaryKey(autoGenerate = true) val monthGoalId: Long,
	@ColumnInfo(name = "account") val account: Long,
	@ColumnInfo(name = "category") val category: Long,
	@ColumnInfo(name = "monthlyLimit") val monthlyLimit: Double,
	@ColumnInfo(name = "yearMonth") val yearMonth: String, // Stored as MM.yyyy format
)
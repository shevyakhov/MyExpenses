package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "MonthGoals",
)
data class MonthGoalDto(
	@PrimaryKey(autoGenerate = true) val monthGoalId: Long,
	@ColumnInfo(name = "account") val account: String,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "category") val category: String,
	@ColumnInfo(name = "monthlyLimit") val monthlyLimit: Double,
	@ColumnInfo(name = "yearMonth") val yearMonth: String, // Stored as MM.yyyy format
)
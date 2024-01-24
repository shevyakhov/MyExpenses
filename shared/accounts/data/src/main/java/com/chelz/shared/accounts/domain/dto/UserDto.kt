package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDto(
	@PrimaryKey(autoGenerate = true) val userId: Long = 0L,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "email") val email: String,
)
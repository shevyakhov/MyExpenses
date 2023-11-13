package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class CategoryDto(
	@PrimaryKey(autoGenerate = true) val categoryId: Long,
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "isEarning") var isEarning: Boolean = false,
	@ColumnInfo(name = "color") var color: String = "#FF0000",
)
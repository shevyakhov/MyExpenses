package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
	tableName = "Operations",
	foreignKeys = [ForeignKey(
		entity = CategoryDto::class,
		parentColumns = ["categoryId"],
		childColumns = ["category"],
		onDelete = ForeignKey.SET_NULL
	), ForeignKey(
		entity = AccountDto::class,
		parentColumns = ["accountId"],
		childColumns = ["account"],
		onDelete = ForeignKey.CASCADE
	)
	]
)

data class OperationDto(
	@PrimaryKey(autoGenerate = true) val id: Long,
	@ColumnInfo(name = "name") val name: String? = null,
	@ColumnInfo(name = "quantity") val quantity: Double,
	@ColumnInfo(name = "category") val category: Long?,
	@ColumnInfo(name = "date") val date: String,
	@ColumnInfo(name = "account") val account: Long,
)
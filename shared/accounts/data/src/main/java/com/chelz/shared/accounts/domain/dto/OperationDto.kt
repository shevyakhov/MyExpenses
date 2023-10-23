package com.chelz.shared.accounts.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
	tableName = "Operations", foreignKeys = [ForeignKey(
		entity = CategoryDto::class,
		parentColumns = ["category"],
		childColumns = ["categoryId"],
		onDelete = ForeignKey.CASCADE
	), ForeignKey(
		entity = AccountDto::class,
		parentColumns = ["account"],
		childColumns = ["accountId"],
		onDelete = ForeignKey.CASCADE
	)
	]
)
data class OperationDto(
	@PrimaryKey(autoGenerate = true) val id: Int,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "quantity") val quantity: Int,
	@ColumnInfo(name = "category") val category: Int,
	@ColumnInfo(name = "date") val date: Long,
	@ColumnInfo(name = "account") val account: Long,
)
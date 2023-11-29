package com.chelz.shared.accounts.domain.entity

data class Category(
	val categoryId: Long,
	var name: String,
	var isEarning: Boolean = false,
	var color: String = "#FF0000",
)
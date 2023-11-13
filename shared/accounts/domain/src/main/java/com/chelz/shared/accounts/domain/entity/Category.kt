package com.chelz.shared.accounts.domain.entity

class Category(
	val categoryId: Long,
	var name: String,
	var isEarning: Boolean = false,
	var color: String = "#FF0000",
)
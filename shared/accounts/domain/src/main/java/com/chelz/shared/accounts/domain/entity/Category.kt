package com.chelz.shared.accounts.domain.entity

class Category(
	val categoryId: Int,
	var name: String,
	var isEarning: Boolean = false,
	var color: String = "#FF0000",
)
package com.chelz.shared.accounts.domain.entity

open class Category(
	val categoryId: Long,
	open var name: String,
	open var isEarning: Boolean = false,
	open var color: String = "#FF0000",
)
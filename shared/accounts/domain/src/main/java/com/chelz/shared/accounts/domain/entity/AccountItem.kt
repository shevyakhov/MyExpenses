package com.chelz.shared.accounts.domain.entity

open class AccountItem(
	open val id: Long,
	open val name: String,
	open val number: String,
	open val color: String,
	open val money: Double,
)
package com.chelz.shared.accounts.domain.entity

data class Account(
	val accountId: Long,
	val name: String,
	val number: String = "",
	val color: String = "#FF0000",
	var money: Double = 0.0,
)
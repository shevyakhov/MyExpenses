package com.chelz.shared.accounts.domain.entity

class Account(
	val accountId: Long,
	val name: String,
	val number: String = "",
	val color: String = "#FF0000",
)
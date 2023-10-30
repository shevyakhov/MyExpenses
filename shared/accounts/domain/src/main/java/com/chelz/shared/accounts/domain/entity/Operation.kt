package com.chelz.shared.accounts.domain.entity

class Operation(
	val id: Long,
	val name: String,
	val quantity: Int,
	val category: Int,
	val date: Long,
	val account: Long,
)

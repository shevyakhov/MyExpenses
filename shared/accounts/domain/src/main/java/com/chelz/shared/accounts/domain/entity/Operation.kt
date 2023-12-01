package com.chelz.shared.accounts.domain.entity

class Operation(
	val id: Long,
	val name: String?,
	val quantity: Double,
	val category: Long?,
	val date: String,
	val account: Long,
)

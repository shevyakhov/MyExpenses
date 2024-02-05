package com.chelz.shared.accounts.domain.entity

data class MonthGoal(
	val id: Long,
	val account: Long,
	val category: Long,
	val monthlyLimit: Double,
	val yearMonth: String,
)
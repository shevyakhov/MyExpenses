package com.chelz.shared.accounts.domain.entity

data class MonthGoal(
	val id: Long,
	val title: String,
	val account: String,
	val category: String,
	val monthlyLimit: Double,
	val yearMonth: String,
)
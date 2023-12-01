package com.chelz.shared.accounts.domain.entity

data class AccountWithUsers(
	val account: Account,
	val users: List<User>,
)

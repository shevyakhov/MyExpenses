package com.chelz.shared.accounts.domain.dto

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "accountId"], tableName = "AccountsWithUsers")
data class AccountWithUsersJunctionDto(
	var userId: Long,
	var accountId: Long,
)
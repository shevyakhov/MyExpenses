package com.chelz.shared.accounts.domain.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AccountWithUsersDto(
	@Embedded val account: AccountDto,
	@Relation(
		parentColumn = "accountId",
		entityColumn = "userId",
		associateBy = Junction(AccountWithUsersJunctionDto::class)
	)
	val users: List<UserDto> = emptyList(),
)
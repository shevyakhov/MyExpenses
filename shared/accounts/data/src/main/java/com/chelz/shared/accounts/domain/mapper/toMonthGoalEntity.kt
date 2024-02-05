package com.chelz.shared.accounts.domain.mapper

import com.chelz.shared.accounts.domain.dto.MonthGoalDto
import com.chelz.shared.accounts.domain.entity.MonthGoal

fun MonthGoalDto.toMonthGoalEntity(): MonthGoal {
	return MonthGoal(
		id = this.monthGoalId,
		account = this.account,
		category = this.category,
		monthlyLimit = this.monthlyLimit,
		yearMonth = this.yearMonth
	)
}

fun List<MonthGoalDto>.toMonthGoalEntities(): List<MonthGoal> {
	return this.map { it.toMonthGoalEntity() }
}

fun MonthGoal.toMonthGoalDto(): MonthGoalDto {
	return MonthGoalDto(
		monthGoalId = this.id,
		account = this.account,
		category = this.category,
		monthlyLimit = this.monthlyLimit,
		yearMonth = this.yearMonth
	)
}

fun List<MonthGoal>.toMonthGoalDtos(): List<MonthGoalDto> {
	return this.map { it.toMonthGoalDto() }
}
package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dto.MonthGoalDto

interface MonthGoalDataSource {

	suspend fun insertMonthGoal(monthGoal: MonthGoalDto)
	suspend fun getMonthGoalById(id: Long): MonthGoalDto?
	suspend fun getMonthGoalByAccountAndCategory(accountId: String, categoryId: String, yearMonth: String): MonthGoalDto?
	suspend fun getAllMonthGoals(): List<MonthGoalDto>
	suspend fun updateMonthGoal(monthGoal: MonthGoalDto)
	suspend fun deleteMonthGoalById(id: Long)
}
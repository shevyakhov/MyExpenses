package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dao.MonthGoalDao
import com.chelz.shared.accounts.domain.dto.MonthGoalDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonthGoalDataSourceImpl(private val monthGoalDao: MonthGoalDao) : MonthGoalDataSource {

	override suspend fun insertMonthGoal(monthGoal: MonthGoalDto) {
		withContext(Dispatchers.IO) {
			monthGoalDao.insertMonthGoal(monthGoal)
		}
	}

	override suspend fun getMonthGoalById(id: Long): MonthGoalDto? {
		return withContext(Dispatchers.IO) {
			monthGoalDao.getMonthGoalById(id)
		}
	}

	override suspend fun getMonthGoalByAccountAndCategory(accountId: Long, categoryId: Long, yearMonth: String): MonthGoalDto? {
		return withContext(Dispatchers.IO) {
			monthGoalDao.getMonthGoalByAccountAndCategory(accountId, categoryId, yearMonth)
		}
	}

	override suspend fun getAllMonthGoals(): List<MonthGoalDto> {
		return withContext(Dispatchers.IO) {
			monthGoalDao.getAllMonthGoals()
		}
	}

	override suspend fun updateMonthGoal(monthGoal: MonthGoalDto) {
		withContext(Dispatchers.IO) {
			monthGoalDao.updateMonthGoal(monthGoal)
		}
	}

	override suspend fun deleteMonthGoalById(id: Long) {
		withContext(Dispatchers.IO) {
			monthGoalDao.deleteMonthGoalById(id)
		}
	}
}

package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chelz.shared.accounts.domain.dto.MonthGoalDto

@Dao
interface MonthGoalDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertMonthGoal(monthGoal: MonthGoalDto)

	@Query("SELECT * FROM MonthGoals WHERE monthGoalId = :id")
	suspend fun getMonthGoalById(id: Long): MonthGoalDto?

	@Query("SELECT * FROM MonthGoals WHERE account = :accountId AND category = :categoryId AND yearMonth = :yearMonth")
	suspend fun getMonthGoalByAccountAndCategory(accountId: Long, categoryId: Long, yearMonth: String): MonthGoalDto?

	@Query("SELECT * FROM MonthGoals")
	suspend fun getAllMonthGoals(): List<MonthGoalDto>

	@Update
	suspend fun updateMonthGoal(monthGoal: MonthGoalDto)

	@Query("DELETE FROM MonthGoals WHERE monthGoalId = :id")
	suspend fun deleteMonthGoalById(id: Long)
}
package com.chelz.shared.accounts.domain.usecase

import com.chelz.shared.accounts.domain.entity.MonthGoal
import com.chelz.shared.accounts.domain.repository.AccountRepository

class InsertMonthGoalUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(monthGoal: MonthGoal) {
		accountRepository.insertMonthGoal(monthGoal)
	}
}

class GetMonthGoalByIdUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(id: Long): MonthGoal? {
		return accountRepository.getMonthGoalById(id)
	}
}

class GetMonthGoalByAccountAndCategoryUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(accountId: String, categoryId: String, yearMonth: String): MonthGoal? {
		return accountRepository.getMonthGoalByAccountAndCategory(accountId, categoryId, yearMonth)
	}
}

class GetAllMonthGoalsUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(): List<MonthGoal> {
		return accountRepository.getAllMonthGoals()
	}
}

class UpdateMonthGoalUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(monthGoal: MonthGoal) {
		accountRepository.updateMonthGoal(monthGoal)
	}
}

class DeleteMonthGoalByIdUseCase(private val accountRepository: AccountRepository) {

	suspend operator fun invoke(id: Long) {
		accountRepository.deleteMonthGoalById(id)
	}
}

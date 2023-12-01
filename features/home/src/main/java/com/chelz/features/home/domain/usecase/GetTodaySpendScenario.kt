package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase
import org.joda.time.LocalDateTime

class GetTodaySpendScenario(
	private val getAllOperationsUseCase: GetAllOperationsUseCase,
) {

	suspend operator fun invoke(): Double {
		val operations = getAllOperationsUseCase.invoke().filter { it.date == LocalDateTime.now().toString("dd.MM.YYYY") }
		val filteredOperations = operations.filter { it.quantity < 0 }
		return GetTodaySpendUseCase(filteredOperations).invoke()
	}
}
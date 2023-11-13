package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase

class GetTodaySpendScenario(
	private val getAllOperationsUseCase: GetAllOperationsUseCase,
) {

	suspend operator fun invoke(): Double {
		val operations = getAllOperationsUseCase.invoke()
		val filteredOperations = operations.filter { it.quantity < 0 }
		return GetTodaySpendUseCase(filteredOperations).invoke()
	}
}
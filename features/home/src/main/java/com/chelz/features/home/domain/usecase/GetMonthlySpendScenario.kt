package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase

class GetMonthlySpendScenario(private val getAllOperationsUseCase: GetAllOperationsUseCase) {

	suspend operator fun invoke(): Map<String, List<Operation>> =
		getAllOperationsUseCase.invoke().groupBy { it.date.drop(5).dropLast(3) }

}
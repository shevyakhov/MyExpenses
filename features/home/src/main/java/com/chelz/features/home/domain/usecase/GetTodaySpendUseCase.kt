package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.entity.Operation
import org.joda.time.DateTime

class GetTodaySpendUseCase(private val operations: List<Operation>) {

	operator fun invoke(): Double {
		val today = DateTime.now().withTimeAtStartOfDay()
		val filtered = operations.filter { DateTime(it.date).withTimeAtStartOfDay() == today }
		return filtered.sumOf { it.quantity }
	}
}
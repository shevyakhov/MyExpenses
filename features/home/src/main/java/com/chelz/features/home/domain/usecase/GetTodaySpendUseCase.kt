package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.entity.Operation
import java.math.RoundingMode
import java.text.DecimalFormat

class GetTodaySpendUseCase(private val operations: List<Operation>) {

	operator fun invoke(): Double {
		val df = DecimalFormat("#.##")
		df.roundingMode = RoundingMode.CEILING
		return df.format(operations.sumOf { it.quantity }).toDouble()
	}
}
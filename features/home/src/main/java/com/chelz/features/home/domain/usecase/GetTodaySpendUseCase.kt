package com.chelz.features.home.domain.usecase

import com.chelz.features.home.presentation.recycler.operations.OperationItem
import java.math.RoundingMode
import java.text.DecimalFormat

class GetTodaySpendUseCase(private val operations: List<OperationItem>) {

	operator fun invoke(): Double {
		val df = DecimalFormat("#.##")
		df.roundingMode = RoundingMode.CEILING
		return df.format(operations.sumOf { it.quantity }).toDouble()
	}
}
package com.chelz.features.home.domain.usecase

import com.chelz.features.home.presentation.recycler.operations.OperationItem
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class GetTodaySpendScenario {

	private val DATE_PATTERN = "dd-MM-yyyy"

	operator fun invoke(list: List<OperationItem>): Double {
		val filteredOperations = list.filter {
			it.quantity < 0 && extractDate(it.date) == LocalDateTime.now().toString(DATE_PATTERN)
		}
		return GetTodaySpendUseCase(filteredOperations).invoke()
	}

	private fun extractDate(dateTimeString: String): String {
		val inputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS")
		val dateTime: DateTime = DateTime.parse(dateTimeString, inputFormatter)

		val outputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy")
		return dateTime.toString(outputFormatter)
	}
}
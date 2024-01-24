package com.chelz.features.home.domain.usecase

import com.chelz.shared.accounts.domain.entity.OperationItem
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class GetWeeklySpendUseCase {

	private val DATE_PATTERN = "dd-MM-yyyy"

	operator fun invoke(list: List<OperationItem>): Map<Int, List<OperationItem>> {
		val currentDate = DateTime.now()
		val startOfWeek = currentDate.withDayOfWeek(1).withTimeAtStartOfDay()
		val endOfWeek = currentDate.withDayOfWeek(7).withTimeAtStartOfDay().plusDays(1)

		val filteredOperations = list.filter {
			it.quantity < 0 && isWithinCurrentWeek(it.date, startOfWeek, endOfWeek)
		}

		val groupedByDay = filteredOperations.groupBy {
			val inputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS")
			val dateTime: DateTime = DateTime.parse(it.date, inputFormatter)
			dateTime.dayOfWeek
		}
		return groupedByDay
	}

	private fun isWithinCurrentWeek(dateTimeString: String, startOfWeek: DateTime, endOfWeek: DateTime): Boolean {
		val inputFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS")
		val dateTime: DateTime = DateTime.parse(dateTimeString, inputFormatter)

		return dateTime.isAfter(startOfWeek) && dateTime.isBefore(endOfWeek)
	}
}
package com.chelz.shared.accounts.domain.entity

class SharedOperation(
	val name: String?,
	val quantity: Double,
	val category: SharedCategory?,
	val date: String,
	val account: String,
)

class SharedOperationItem(
	override val name: String?,
	override val quantity: Double,
	override val category: SharedCategory?,
	override val date: String,
	override val account: String,
) : OperationItem(
	id = -1L, name = name, quantity = quantity, category = category, date = date, account = account
)

fun List<SharedOperation>.toOperationItem(): List<SharedOperationItem> =
	map { SharedOperationItem(it.name, it.quantity, it.category, it.date, it.account) }

package com.chelz.features.home.domain

import com.chelz.features.home.presentation.recycler.accounts.AccountItem

class SharedAccount(
	val accountId: String = "",
	val name: String = "",
	val number: String = "",
	val color: String = "#FF0000",
	var money: Double = 0.0,
	var hostEmail: String,
	var operations: List<SharedOperation> = emptyList(),
)

class SharedAccountItem(
	override val id: Long = -1L,
	val sharedId: String,
	override val name: String,
	override val number: String,
	override val color: String,
	override val money: Double,
	var operations: List<SharedOperation> = emptyList(),
) : AccountItem(id, name, number, color, money)

internal fun List<SharedAccount>.toAccountItem(): List<AccountItem> =
	map {
		SharedAccountItem(
			sharedId = it.accountId,
			name = it.name,
			number = it.number,
			color = it.color,
			money = it.money,
			operations = it.operations
		)
	}
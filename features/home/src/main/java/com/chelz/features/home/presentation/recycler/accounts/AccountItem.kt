package com.chelz.features.home.presentation.recycler.accounts

import com.chelz.shared.accounts.domain.entity.Account

data class AccountItem(
	val id: Long,
	val name: String,
	val number: String,
	val color: String,
	val money: Double,
)

internal fun AccountItem.toAccount() =
	Account(id, name, number, color, money)

internal fun List<Account>.toSliderItem(): List<AccountItem> =
	map { AccountItem(it.accountId, it.name, it.number, it.color, it.money) }
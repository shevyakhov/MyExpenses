package com.chelz.features.home.presentation.recycler.accounts

import com.chelz.shared.accounts.domain.entity.Account

open class AccountItem(
	open val id: Long,
	open val name: String,
	open val number: String,
	open val color: String,
	open val money: Double,
)

internal fun AccountItem.toAccount() =
	Account(id, name, number, color, money)

internal fun List<Account>.toAccountItem(): List<AccountItem> =
	map { AccountItem(it.accountId, it.name, it.number, it.color, it.money) }


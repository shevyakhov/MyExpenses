package com.chelz.shared.accounts.domain.entity

fun List<Account>.toAccountItem(): List<AccountItem> =
	map { AccountItem(it.accountId, it.name, it.number, it.color, it.money) }

fun AccountItem.toAccount() =
	Account(id, name, number, color, money)

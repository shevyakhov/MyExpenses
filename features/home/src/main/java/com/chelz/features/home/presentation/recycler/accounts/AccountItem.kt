package com.chelz.features.home.presentation.recycler.accounts

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem

internal fun AccountItem.toAccount() =
	Account(id, name, number, color, money)

internal fun List<Account>.toAccountItem(): List<AccountItem> =
	map { AccountItem(it.accountId, it.name, it.number, it.color, it.money) }


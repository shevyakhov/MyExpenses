package com.chelz.features.qrscanner.presentation.result.adapter

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem

internal fun AccountItem.toAccount() =
	Account(id, name, number, color, money)

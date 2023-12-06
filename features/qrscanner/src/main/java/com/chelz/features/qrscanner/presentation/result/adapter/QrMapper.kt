package com.chelz.features.qrscanner.presentation.result.adapter

import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Operation
import org.joda.time.LocalDateTime

fun ItemsEntity.toQrItem() =
	list.map {
		QrItem(name = it.name, sum = it.sum, price = it.price, quantity = it.quantity)
	}

internal fun QrItem.toDto(account: Account): Operation {
	val price = price!! / -100.0
	return Operation(
		id = 0L,
		name = name ?: category?.name ?: "?",
		quantity = price,
		category = category?.categoryId.let { id -> if (id == -1L) null else id },
		date = LocalDateTime.now().toString("dd-MM-yyyy HH:mm:ss.SSS"),
		account = account.accountId
	)
}
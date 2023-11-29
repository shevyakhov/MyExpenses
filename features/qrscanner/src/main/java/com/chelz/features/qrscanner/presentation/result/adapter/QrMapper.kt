package com.chelz.features.qrscanner.presentation.result.adapter

import com.chelz.features.qrscanner.domain.entity.ItemsEntity

fun ItemsEntity.toQrItem() =
	list.map {
		QrItem(name = it.name, sum = it.sum, price = it.price, quantity = it.quantity)
	}

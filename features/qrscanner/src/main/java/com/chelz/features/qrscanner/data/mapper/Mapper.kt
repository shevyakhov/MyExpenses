package com.chelz.features.qrscanner.data.mapper

import com.chelz.features.qrscanner.data.dto.Items
import com.chelz.features.qrscanner.data.dto.ReceiptResult
import com.chelz.features.qrscanner.domain.entity.HtmlEntity
import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.features.qrscanner.domain.entity.SingleItemEntity

internal fun ReceiptResult.toItemEntities(): ItemsEntity =
	ItemsEntity(
		data?.json?.items?.toSingleItems() ?: emptyList()
	)

internal fun ReceiptResult.toHtmlEntity(): HtmlEntity =
	HtmlEntity(data?.html.toString())

internal fun ArrayList<Items>.toSingleItems() =
	this.map {
		SingleItemEntity(
			sum = it.sum,
			name = it.name,
			price = it.price?.toDouble(),
			quantity = it.quantity
		)
	}
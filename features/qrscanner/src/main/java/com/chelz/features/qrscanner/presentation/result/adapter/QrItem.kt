package com.chelz.features.qrscanner.presentation.result.adapter

import com.chelz.shared.accounts.domain.entity.Category

data class QrItem(
	var sum: Int? = null,
	var name: String? = null,
	var price: Int? = null,
	var quantity: Double? = null,
	var category: Category? = null,
)
package com.chelz.features.qrscanner.domain.entity

import java.io.Serializable

data class ItemsEntity(
	val list: List<SingleItemEntity>,
) : Serializable

data class SingleItemEntity(
	var sum: Int? = null,
	var name: String? = null,
	var price: Int? = null,
	var quantity: Double? = null,
) : Serializable
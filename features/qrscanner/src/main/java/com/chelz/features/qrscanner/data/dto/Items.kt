package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Items(

	@SerializedName("sum") var sum: Int? = null,
	@SerializedName("name") var name: String? = null,
	@SerializedName("price") var price: Int? = null,
	@SerializedName("ndsSum") var ndsSum: Int? = null,
	@SerializedName("quantity") var quantity: Int? = null,
	@SerializedName("paymentType") var paymentType: Int? = null,
	@SerializedName("productType") var productType: Int? = null,
	@SerializedName("itemsQuantityMeasure") var itemsQuantityMeasure: Int? = null
)
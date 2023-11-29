package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Items(

	@SerializedName("sum") var sum: Int? = null,
	@SerializedName("name") var name: String? = null,
	@SerializedName("price") var price: Double? = null,
	@SerializedName("quantity") var quantity: Int? = null,
	@SerializedName("modifiers") var modifiers: ArrayList<String> = arrayListOf(),
	@SerializedName("properties") var properties: ArrayList<String> = arrayListOf(),

	)
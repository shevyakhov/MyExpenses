package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class ReceiveDate(
	@SerializedName("date") var date: Int? = null,
)
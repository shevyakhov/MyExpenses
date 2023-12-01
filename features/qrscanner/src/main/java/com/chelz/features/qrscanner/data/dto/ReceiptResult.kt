package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class ReceiptResult(
	@SerializedName("code") var code: Int? = null,
	@SerializedName("first") var first: Int? = null,
	@SerializedName("data") var data: Data? = Data(),
	@SerializedName("request") var request: Request? = Request(),
)
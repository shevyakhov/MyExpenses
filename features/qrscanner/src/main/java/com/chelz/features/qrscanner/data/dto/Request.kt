package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Request(
	@SerializedName("qrurl") var qrurl: String? = null,
	@SerializedName("qrfile") var qrfile: String? = null,
	@SerializedName("qrraw") var qrraw: String? = null,
	@SerializedName("manual") var manual: Manual? = Manual()
)
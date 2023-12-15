package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Data(
	@SerializedName("json") var json: JsonOutput? = JsonOutput(),
	@SerializedName("html") var html: String? = null,
)
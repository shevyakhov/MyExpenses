package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Manual(
	@SerializedName("fn") var fn: String? = null,
	@SerializedName("fd") var fd: String? = null,
	@SerializedName("fp") var fp: String? = null,
	@SerializedName("check_time") var checkTime: String? = null,
	@SerializedName("type") var type: String? = null,
	@SerializedName("sum") var sum: String? = null
)
package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Id(
	@SerializedName("$\\oid") var oid: String? = null,
)
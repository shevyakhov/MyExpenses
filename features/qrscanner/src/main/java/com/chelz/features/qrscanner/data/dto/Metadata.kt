package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Metadata(

	@SerializedName("id") var id: Long? = null,
	@SerializedName("ofdId") var ofdId: String? = null,
	@SerializedName("address") var address: String? = null,
	@SerializedName("subtype") var subtype: String? = null,
	@SerializedName("receiveDate") var receiveDate: String? = null
)

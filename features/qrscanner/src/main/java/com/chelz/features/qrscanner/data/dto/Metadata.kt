package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class Metadata(

	@SerializedName("id") var id: String? = null,
	@SerializedName("_id") var Id: Id? = Id(),
	@SerializedName("fsId") var fsId: String? = null,
	@SerializedName("ofdId") var ofdId: String? = null,
	@SerializedName("subtype") var subtype: String? = null,
	@SerializedName("kktRegId") var kktRegId: String? = null,
	@SerializedName("documentId") var documentId: Int? = null,
	@SerializedName("receiveDate") var receiveDate: ReceiveDate? = ReceiveDate(),
	@SerializedName("v2ValidateErr") var v2ValidateErr: String? = null,
	@SerializedName("protocolVersion") var protocolVersion: String? = null,
	@SerializedName("protocolSubversion") var protocolSubversion: Int? = null,

	)

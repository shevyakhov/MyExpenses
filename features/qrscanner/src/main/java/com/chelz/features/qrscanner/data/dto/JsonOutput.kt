package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class JsonOutput(
	@SerializedName("user") var user: String? = null,
	@SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
	@SerializedName("nds10") var nds10: Int? = null,
	@SerializedName("nds18") var nds18: Int? = null,
	@SerializedName("message") var message: ArrayList<String> = arrayListOf(),
	@SerializedName("rawData") var rawData: String? = null,
	@SerializedName("userInn") var userInn: String? = null,
	@SerializedName("dateTime") var dateTime: String? = null,
	@SerializedName("kktRegId") var kktRegId: String? = null,
	@SerializedName("metadata") var metadata: Metadata? = Metadata(),
	@SerializedName("operator") var operator: String? = null,
	@SerializedName("totalSum") var totalSum: Int? = null,
	@SerializedName("modifiers") var modifiers: ArrayList<String> = arrayListOf(),
	@SerializedName("fiscalSign") var fiscalSign: Int? = null,
	@SerializedName("properties") var properties: ArrayList<String> = arrayListOf(),
	@SerializedName("receiptCode") var receiptCode: Int? = null,
	@SerializedName("shiftNumber") var shiftNumber: Int? = null,
	@SerializedName("stornoItems") var stornoItems: ArrayList<String> = arrayListOf(),
	@SerializedName("cashTotalSum") var cashTotalSum: Int? = null,
	@SerializedName("taxationType") var taxationType: Int? = null,
	@SerializedName("ecashTotalSum") var ecashTotalSum: Int? = null,
	@SerializedName("operationType") var operationType: Int? = null,
	@SerializedName("requestNumber") var requestNumber: Int? = null,
	@SerializedName("senderAddress") var senderAddress: String? = null,
	@SerializedName("fiscalDriveNumber") var fiscalDriveNumber: String? = null,
	@SerializedName("retailPlaceAddress") var retailPlaceAddress: String? = null,
	@SerializedName("fiscalDocumentNumber") var fiscalDocumentNumber: Int? = null,
)

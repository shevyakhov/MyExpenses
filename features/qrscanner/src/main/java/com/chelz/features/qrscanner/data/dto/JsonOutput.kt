package com.chelz.features.qrscanner.data.dto

import com.google.gson.annotations.SerializedName

data class JsonOutput(
	@SerializedName("code") var code: Long? = null,
	@SerializedName("user") var user: String? = null,
	@SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
	@SerializedName("nds18") var nds18: Long? = null,
	@SerializedName("region") var region: String? = null,
	@SerializedName("userInn") var userInn: String? = null,
	@SerializedName("dateTime") var dateTime: String? = null,
	@SerializedName("kktRegId") var kktRegId: String? = null,
	@SerializedName("metadata") var metadata: Metadata? = Metadata(),
	@SerializedName("operator") var operator: String? = null,
	@SerializedName("totalSum") var totalSum: Long? = null,
	@SerializedName("creditSum") var creditSum: Long? = null,
	@SerializedName("numberKkt") var numberKkt: String? = null,
	@SerializedName("fiscalSign") var fiscalSign: Long? = null,
	@SerializedName("prepaidSum") var prepaidSum: Long? = null,
	@SerializedName("retailPlace") var retailPlace: String? = null,
	@SerializedName("shiftNumber") var shiftNumber: Long? = null,
	@SerializedName("cashTotalSum") var cashTotalSum: Long? = null,
	@SerializedName("provisionSum") var provisionSum: Long? = null,
	@SerializedName("ecashTotalSum") var ecashTotalSum: Long? = null,
	@SerializedName("operationType") var operationType: Long? = null,
	@SerializedName("redefine_mask") var redefineMask: Long? = null,
	@SerializedName("requestNumber") var requestNumber: Long? = null,
	@SerializedName("fiscalDriveNumber") var fiscalDriveNumber: String? = null,
	@SerializedName("messageFiscalSign") var messageFiscalSign: String? = null,
	@SerializedName("retailPlaceAddress") var retailPlaceAddress: String? = null,
	@SerializedName("appliedTaxationType") var appliedTaxationType: Long? = null,
	@SerializedName("buyerPhoneOrAddress") var buyerPhoneOrAddress: String? = null,
	@SerializedName("fiscalDocumentNumber") var fiscalDocumentNumber: Long? = null,
	@SerializedName("fiscalDocumentFormatVer") var fiscalDocumentFormatVer: Long? = null
)

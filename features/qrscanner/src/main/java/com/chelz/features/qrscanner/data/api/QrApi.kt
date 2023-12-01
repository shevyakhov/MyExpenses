package com.chelz.features.qrscanner.data.api

import com.chelz.features.qrscanner.data.dto.ReceiptResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QrApi {

	@FormUrlEncoded
	@POST("api/v1/check/get")
	suspend fun getQrResult(
		@Field("token") token: String,
		@Field("qrraw") qrraw: String,
	): ReceiptResult
}
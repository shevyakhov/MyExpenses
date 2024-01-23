package com.chelz.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class SourceDto(

	@SerializedName("id") var id: String? = null,
	@SerializedName("name") var name: String? = null,

	)
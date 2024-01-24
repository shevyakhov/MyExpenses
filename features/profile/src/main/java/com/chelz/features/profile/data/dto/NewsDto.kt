package com.chelz.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class NewsDto(
	@SerializedName("status") var status: String? = null,
	@SerializedName("totalResults") var totalResults: Int? = null,
	@SerializedName("articles") var articles: ArrayList<ArticlesDto> = arrayListOf(),
)
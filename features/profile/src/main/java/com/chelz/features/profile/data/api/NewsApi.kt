package com.chelz.features.profile.data.api

import com.chelz.features.profile.data.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

	@GET("./everything")
	suspend fun searchArticles(
		@Query("apiKey") apiKey: String? = null,
		@Query("q") query: String = "финанс",
		@Query("searchIn") searchIn: String? = null,
		@Query("sources") sources: String? = null,
		@Query("domains") domains: String? = null,
		@Query("excludeDomains") excludeDomains: String? = null,
		@Query("from") from: String? = null,
		@Query("to") to: String? = null,
		@Query("language") language: String = "ru",
		@Query("sortBy") sortBy: String? = null,
		@Query("pageSize") pageSize: Int? = null,
		@Query("page") page: Int? = null,
	): NewsDto
}
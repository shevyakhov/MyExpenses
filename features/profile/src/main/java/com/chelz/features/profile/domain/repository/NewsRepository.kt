package com.chelz.features.profile.domain.repository

import com.chelz.features.profile.domain.entity.NewsEntity

interface NewsRepository {

	suspend fun searchArticles(
		apiKey: String?,
		query: String = "финансы",
		searchIn: String? = null,
		sources: String? = null,
		domains: String? = null,
		excludeDomains: String? = null,
		from: String? = null,
		to: String? = null,
		language: String = "ru",
		sortBy: String? = null,
		pageSize: Int? = 10,
		page: Int? = null,
	): NewsEntity
}
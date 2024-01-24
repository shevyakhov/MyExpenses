package com.chelz.features.profile.data.repository

import com.chelz.features.profile.data.datasource.NewsDataSource
import com.chelz.features.profile.domain.entity.NewsEntity
import com.chelz.features.profile.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsDataSource: NewsDataSource) : NewsRepository {

	override suspend fun searchArticles(
		apiKey: String?,
		query: String,
		searchIn: String?,
		sources: String?,
		domains: String?,
		excludeDomains: String?,
		from: String?,
		to: String?,
		language: String,
		sortBy: String?,
		pageSize: Int?,
		page: Int?,
	): NewsEntity {
		return newsDataSource.searchArticles(
			apiKey = apiKey,
			query = query,
			searchIn = searchIn,
			sources = sources,
			domains = domains,
			excludeDomains = excludeDomains,
			from = from,
			to = to,
			language = language,
			sortBy = sortBy,
			pageSize = pageSize,
			page = page
		)
	}
}
package com.chelz.features.profile.domain.usecase

import com.chelz.features.profile.domain.entity.NewsEntity
import com.chelz.features.profile.domain.repository.NewsRepository

class NewsUseCase(private val newsRepository: NewsRepository) {

	suspend operator fun invoke(
		apiKey: String? = null,
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
	): NewsEntity {
		return newsRepository.searchArticles(
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
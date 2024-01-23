package com.chelz.features.profile.data.datasource

import com.chelz.features.profile.data.api.NewsApi
import com.chelz.features.profile.data.mapper.toEntity
import com.chelz.features.profile.domain.entity.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsDataSourceImpl(private val newsApi: NewsApi) : NewsDataSource {

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
    ): NewsEntity = withContext(Dispatchers.IO) {
		newsApi.searchArticles(
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
		).toEntity()
	}
}
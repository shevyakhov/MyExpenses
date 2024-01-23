package com.chelz.features.profile.data.mapper

import com.chelz.features.profile.data.dto.ArticlesDto
import com.chelz.features.profile.data.dto.NewsDto
import com.chelz.features.profile.data.dto.SourceDto
import com.chelz.features.profile.domain.entity.ArticlesEntity
import com.chelz.features.profile.domain.entity.NewsEntity
import com.chelz.features.profile.domain.entity.SourceEntity

// Mapper for NewsDto to NewsEntity
fun NewsDto.toEntity(): NewsEntity {
	val articlesEntities = articles.map { it.toEntity() }
	return NewsEntity(
		status = status,
		totalResults = totalResults,
		articles = articlesEntities
	)
}

// Mapper for NewsEntity to NewsDto
fun NewsEntity.toDto(): NewsDto {
	val articlesDto = articles.map { it.toDto() }
	return NewsDto(
		status = status,
		totalResults = totalResults,
		articles = ArrayList(articlesDto)
	)
}

// Mapper for ArticlesDto to ArticlesEntity
fun ArticlesDto.toEntity(): ArticlesEntity {
	return ArticlesEntity(
		source = source?.toEntity(),
		author = author,
		title = title,
		description = description,
		url = url,
		urlToImage = urlToImage,
		publishedAt = publishedAt,
		content = content
	)
}

// Mapper for ArticlesEntity to ArticlesDto
fun ArticlesEntity.toDto(): ArticlesDto {
	return ArticlesDto(
		source = source?.toDto(),
		author = author,
		title = title,
		description = description,
		url = url,
		urlToImage = urlToImage,
		publishedAt = publishedAt,
		content = content
	)
}

// Mapper for Source to SourceEntity
fun SourceDto.toEntity(): SourceEntity {
	return SourceEntity(
		id = id,
		name = name
	)
}

// Mapper for SourceEntity to Source
fun SourceEntity.toDto(): SourceDto {
	return SourceDto(
		id = id,
		name = name
	)
}

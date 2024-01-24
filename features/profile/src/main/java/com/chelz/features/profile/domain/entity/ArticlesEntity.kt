package com.chelz.features.profile.domain.entity

data class ArticlesEntity(

	val source: SourceEntity?,
	val author: String?,
	val title: String?,
	val description: String?,
	val url: String?,
	val urlToImage: String?,
	val publishedAt: String?,
	val content: String?,
)
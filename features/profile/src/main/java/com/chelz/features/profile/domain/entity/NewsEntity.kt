package com.chelz.features.profile.domain.entity

data class NewsEntity(

    val status: String?,
    val totalResults: Int?,
    val articles: List<ArticlesEntity>,
)
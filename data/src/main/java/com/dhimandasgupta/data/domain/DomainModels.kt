package com.dhimandasgupta.data.domain

import java.lang.Exception

data class NewsDomainModel(
    val articles: List<ArticleDomainModel> = emptyList(),
    val exception: ExceptionDomainModel? = null
)

data class ArticleDomainModel(
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String
)

data class ExceptionDomainModel(
    val exception: Exception,
    val source: SourceDomainModel
)

data class SourceDomainModel(
    val sourceName: String,
    val selected: Boolean = true
)

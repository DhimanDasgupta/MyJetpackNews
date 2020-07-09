package com.dhimandasgupta.data.presentaion

import java.lang.Exception

sealed class UIModels

object IdleUIModel : UIModels()

data class LoadingUIModel(val source: Source) : UIModels()

data class ErrorUIModel(val exception: Exception, val source: Source) : UIModels()

data class SuccessUIModel(val articlesUIModel: ArticlesUIModel) : UIModels()

data class ArticlesUIModel(val articles: List<ArticleUIModel> = emptyList())

data class ArticleUIModel(
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class Source(
    val title: String,
    val selected: Boolean = false
)

val sources = listOf(
    Source("google"),
    Source("apple"),
    Source("amazon"),
    Source("Bitcoinist"),
    Source("newsBTC"),
    Source("CNN"),
    Source("Reuters"),
    Source("CBS17.com"),
    Source("Engadget"),
    Source("Google News"),
    Source("BBC"),
    Source("Electrek"),
    Source("KOB")
)

fun mapSource(query: String) = sources.map { source: Source ->
    source.copy(selected = query == source.title)
}.toList()

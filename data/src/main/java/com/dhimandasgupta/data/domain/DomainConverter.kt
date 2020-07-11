package com.dhimandasgupta.data.domain

import com.dhimandasgupta.data.data.models.Article
import com.dhimandasgupta.data.data.models.NetworkResponse

fun NetworkResponse.toDomainModel(): NewsDomainModel {
    val articleDomainModels = mutableListOf<ArticleDomainModel>()

    articles.map { articleNetworkModel ->
        val article = articleNetworkModel.isValidArticle()
        article?.let {
            articleDomainModels.add(
                ArticleDomainModel(
                    sourceName = articleNetworkModel.source.name ?: "",
                    author = articleNetworkModel.author ?: "",
                    title = articleNetworkModel.title ?: "",
                    description = articleNetworkModel.description ?: "",
                    url = articleNetworkModel.url ?: "",
                    urlToImage = articleNetworkModel.urlToImage ?: "",
                    publishedAt = articleNetworkModel.publishedAt ?: "",
                    content = articleNetworkModel.content ?: ""
                )
            )
        }
    }
    return NewsDomainModel(articleDomainModels.toList())
}

fun errorDomainModel(sourceName: String, e: Exception) = NewsDomainModel(
    articles = emptyList(),
    exception = ExceptionDomainModel(exception = e, source = SourceDomainModel(sourceName))
)

private fun Article.isValidArticle(): Article? {
    return if (source.name?.isNotBlank() == true
        && author?.isNotBlank() == true
        && title?.isNotBlank() == true
        && description?.isNotBlank() == true
        && url?.isNotBlank() == true
        && urlToImage?.isNotBlank() == true
        && publishedAt?.isNotBlank() == true
        && content?.isNotBlank() == true
    )
        this
    else null
}
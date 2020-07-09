package com.dhimandasgupta.data.domain

import com.dhimandasgupta.data.data.models.NetworkResponse
import java.lang.Exception

fun NetworkResponse.toDomainModel(): NewsDomainModel {
    val articleDomainModels = mutableListOf<ArticleDomainModel>()

    this.articles.map { articleNetworkModel ->
        articleDomainModels.add(
            ArticleDomainModel(
                sourceName = articleNetworkModel.source.name,
                author = articleNetworkModel.author,
                title = articleNetworkModel.title,
                description = articleNetworkModel.description,
                url = articleNetworkModel.url,
                urlToImage = articleNetworkModel.urlToImage,
                publishedAt = articleNetworkModel.publishedAt,
                content = articleNetworkModel.content
            )
        )
    }
    return NewsDomainModel(articleDomainModels.toList())
}

fun errorDomainModel(sourceName: String, e: Exception) = NewsDomainModel(
    articles = emptyList(),
    exception = ExceptionDomainModel(exception = e, source = SourceDomainModel(sourceName))
)
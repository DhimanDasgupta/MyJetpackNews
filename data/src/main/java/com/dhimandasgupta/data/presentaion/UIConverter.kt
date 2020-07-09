package com.dhimandasgupta.data.presentaion

import com.dhimandasgupta.data.domain.ExceptionDomainModel
import com.dhimandasgupta.data.domain.NewsDomainModel

fun NewsDomainModel.toUIModel(): UIModels {
    if (articles.isEmpty() && exception != null) return errorUiModel(exception)

    val articleUIModels = mutableListOf<ArticleUIModel>()

    this.articles.map { articleDomainModel ->
        articleUIModels.add(
            ArticleUIModel(
                sourceName = articleDomainModel.sourceName,
                author = articleDomainModel.author,
                title = articleDomainModel.title,
                description = articleDomainModel.description,
                url = articleDomainModel.url,
                urlToImage = articleDomainModel.urlToImage,
                publishedAt = articleDomainModel.publishedAt,
                content = articleDomainModel.content
            )
        )
    }

    return SuccessUIModel(ArticlesUIModel(articleUIModels.toList()))
}

private fun errorUiModel(exception: ExceptionDomainModel): UIModels {
    return ErrorUIModel(exception = exception.exception, source = Source(title = exception.source.sourceName, selected = true))
}
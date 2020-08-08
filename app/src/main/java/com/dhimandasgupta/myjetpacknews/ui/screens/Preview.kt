package com.dhimandasgupta.myjetpacknews.ui.screens

import com.dhimandasgupta.data.presentaion.ArticleUIModel
import com.dhimandasgupta.data.presentaion.ArticlesUIModel
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.IdleUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel

val previewSourceSelected = Source(title = "Google", selected = true)

val previewSourceUnSelected = Source(title = "Google", selected = false)

val previewArticles: List<ArticleUIModel> = listOf(
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    ),
    ArticleUIModel(
        sourceName = "Source Name",
        author = "Author name",
        title = "Title",
        url = "https://google.com",
        urlToImage = "https://imgur.com/oKIqpfl",
        content = "Some random content",
        description = "Some random description",
        publishedAt = "Some time"
    )
)

val previewIdleUiModels = IdleUIModel

val previewLoadingUiModels = LoadingUIModel(source = previewSourceSelected)

val previewSuccessUIModel = SuccessUIModel(ArticlesUIModel(articles = previewArticles))

val previewErrorUiModels = ErrorUIModel(Exception("Some random exception"), source = previewSourceSelected)

val previewSources = listOf(
    previewSourceSelected,
    previewSourceUnSelected,
    previewSourceSelected,
    previewSourceUnSelected,
    previewSourceSelected,
    previewSourceUnSelected,
    previewSourceSelected,
    previewSourceUnSelected
)

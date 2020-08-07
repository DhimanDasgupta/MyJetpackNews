package com.dhimandasgupta.myjetpacknews.ui.main

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.data.presentaion.ArticleUIModel
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.IdleUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.data.presentaion.UIModels
import com.dhimandasgupta.data.presentaion.initialNewsUiState
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.common.shapes
import com.dhimandasgupta.myjetpacknews.viewmodel.NewsViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@Composable
fun ThemedApp(newsViewModel: NewsViewModel) {
    MyNewsTheme {
        App(newsViewModel = newsViewModel)
    }
}

@Composable
fun App(newsViewModel: NewsViewModel) {
    Scaffold {
        Column {
            Content(newsViewModel = newsViewModel)
        }
    }
}

@Composable
fun Content(newsViewModel: NewsViewModel) {
    val newsUiState = newsViewModel.newsUiState.observeAsState(initial = initialNewsUiState)

    NewsTopAppBar(source = newsUiState.value.currentSource)
    NewsBody(uiModels = newsUiState.value.uiModels)
    NewsBottomAppBar(sources = newsUiState.value.allSources) { source ->
        if (!source.selected) newsViewModel.fetchNewsFromSource(source)
    }
}

@Composable
fun NewsTopAppBar(source: Source) {
    TopAppBar {
        Text(
            text = stringResource(id = R.string.news_from, formatArgs = arrayOf(source.title)),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
        )
    }
}

@Composable
fun NewsBody(uiModels: UIModels) {
    Box(
        Modifier.weight(weight = 1.0f, fill = true),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        NewsContainer(uiModels = uiModels)
    }
}

@Composable
fun NewsContainer(uiModels: UIModels) {
    when (uiModels) {
        is IdleUIModel -> RenderIdle()
        is LoadingUIModel -> RenderLoading(source = uiModels.source)
        is SuccessUIModel -> RenderArticles(articles = uiModels.articlesUIModel.articles)
        is ErrorUIModel -> RenderError(errorUIModel = uiModels)
    }
}

@Composable
fun RenderIdle() {
    Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
        Column {
            CircularProgressIndicator(modifier = Modifier.gravity(Alignment.CenterHorizontally))
            Text(
                text = stringResource(id = R.string.idle_text),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RenderLoading(source: Source) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), gravity = ContentGravity.Center) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier.gravity(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.loading_text),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = source.title,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RenderArticles(articles: List<ArticleUIModel>) {
    ScrollableColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        articles.map {
            RenderArticle(article = it)
        }
    }
}

@Composable
fun RenderArticle(article: ArticleUIModel) {
    val showDialog = mutableStateOf(false)
    if (showDialog.value) {
        ShowArticleInADialog(article = article) { showDialog.value = false }
    }

    Spacer(modifier = Modifier.wrapContentWidth().wrapContentHeight().preferredSize(8.dp))
    Card(
        shape = shapes.medium,
        elevation = 8.dp,
        color = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth().clickable(enabled = true, indication = RippleIndication(bounded = true), onClick = { showDialog.value = true })
    ) {
        Column {
            Spacer(
                modifier = Modifier.wrapContentWidth().wrapContentHeight().preferredSize(8.dp)
            )
            Row {
                Spacer(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight()
                        .preferredSize(8.dp)
                )
                CoilImageWithCrossfade(
                    data = article.urlToImage,
                    modifier = Modifier.preferredSize(100.dp)
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(
                        modifier = Modifier.wrapContentWidth().wrapContentHeight()
                            .preferredSize(4.dp)
                    )
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(
                        modifier = Modifier.wrapContentWidth().wrapContentHeight()
                            .preferredSize(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RenderError(errorUIModel: ErrorUIModel) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), gravity = ContentGravity.Center) {
        Column {
            Text(
                text = stringResource(id = R.string.error_text),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().gravity(align = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.preferredHeight(4.dp))
            Text(
                text = errorUIModel.source.title,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().gravity(align = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.preferredHeight(8.dp))
            Text(
                text = stringResource(
                    id = R.string.error_due_to,
                    formatArgs = arrayOf(errorUIModel.exception.localizedMessage)
                ),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().gravity(align = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun NewsBottomAppBar(sources: List<Source>, onSourceSelected: (Source) -> Unit) {
    BottomAppBar(modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)) {
        ScrollableRow(modifier = Modifier.fillMaxHeight()) {
            sources.map { source ->
                BottomAppBarItem(
                    source = source,
                    onSourceSelected = onSourceSelected
                )
            }
        }
    }
}

@Composable
fun BottomAppBarItem(source: Source, onSourceSelected: (Source) -> Unit) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 0.dp)
            .clickable(
                enabled = true,
                indication = RippleIndication(bounded = true),
                onClick = { onSourceSelected.invoke(source) }
            )
    ) {
        if (source.selected) {
            Box(
                modifier = Modifier.width(32.dp).height(2.dp)
                    .gravity(align = Alignment.CenterHorizontally),
                shape = shapes.medium,
                backgroundColor = MaterialTheme.colors.onPrimary
            )
        }
        Text(
            text = source.title,
            style = if (source.selected) MaterialTheme.typography.h5 else MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
                .weight(1f, true)
        )
        if (source.selected) {
            Box(
                modifier = Modifier.width(64.dp).height(2.dp)
                    .gravity(align = Alignment.CenterHorizontally),
                shape = shapes.medium,
                backgroundColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun ShowArticleInADialog(article: ArticleUIModel, onDismiss: () -> Unit) {
    MyNewsTheme {
        AlertDialog(
            onCloseRequest = onDismiss,
            text = {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.body2
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        )
    }
}

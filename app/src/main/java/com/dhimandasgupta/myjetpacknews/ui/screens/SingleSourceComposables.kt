package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Icon
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
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Center
import androidx.compose.ui.text.style.TextDecoration
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
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@Composable
fun SingleSourceScreen(singleSourceViewModel: SingleSourceViewModel, onUpClicked: () -> Unit, onNewsClicked: (String) -> Unit) {
    MyNewsTheme {
        ThemedSingleSourceScreen(singleSourceViewModel = singleSourceViewModel, onUpClicked = onUpClicked, onNewsClicked = onNewsClicked)
    }
}

@Composable
fun ThemedSingleSourceScreen(singleSourceViewModel: SingleSourceViewModel, onUpClicked: () -> Unit, onNewsClicked: (String) -> Unit) {
    val newsUiState = singleSourceViewModel.newsUiState.observeAsState(initial = initialNewsUiState)

    Scaffold(
        topBar = { NewsTopAppBar(source = newsUiState.value.currentSource, onUpClicked = onUpClicked) },
        bodyContent = {
            NewsBody(
                uiModels = newsUiState.value.uiModels,
                sources = newsUiState.value.allSources,
                onNewsClicked = onNewsClicked
            ) { source ->
                if (!source.selected) singleSourceViewModel.fetchNewsFromSource(source)
            }
        },
        bottomBar = {
            NewsBottomAppBar(sources = newsUiState.value.allSources) { source ->
                if (!source.selected) singleSourceViewModel.fetchNewsFromSource(source)
            }
        }
    )
}

@Composable
fun NewsTopAppBar(source: Source, onUpClicked: () -> Unit) {
    TopAppBar {
        IconButton(
            onClick = { onUpClicked.invoke() },
            modifier = Modifier.wrapContentSize(align = Alignment.Center).gravity(align = CenterVertically),
            icon = { Icon(asset = vectorResource(id = R.drawable.ic_arrow_back), tint = colors.onPrimary) }
        )
        Text(
            text = stringResource(id = R.string.news_from, formatArgs = arrayOf(source.title)),
            style = typography.h5,
            color = colors.onPrimary,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
        )
    }
}

@Composable
fun NewsBody(uiModels: UIModels, sources: List<Source>, onNewsClicked: (String) -> Unit, onSourceSelected: (Source) -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        backgroundColor = colors.surface,
    ) {
        val isLandscape = ConfigurationAmbient.current.orientation == ORIENTATION_LANDSCAPE
        val leftSourcesWeight = if (isLandscape) 0.2f else 0.0f

        Row(modifier = Modifier.fillMaxWidth()) {
            if (isLandscape) {
                LazyColumnFor(
                    items = sources,
                    modifier = Modifier.weight(
                        leftSourcesWeight, true
                    ).fillMaxHeight().padding(start = 8.dp)
                ) {
                    BottomAppBarItem(
                        source = it,
                        onSourceSelected = onSourceSelected
                    )
                }
            }

            Box(modifier = Modifier.weight(1f - leftSourcesWeight, true)) {
                NewsContainer(uiModels = uiModels, onNewsClicked)
            }
        }
    }
}

@Composable
fun NewsContainer(uiModels: UIModels, onNewsClicked: (String) -> Unit) {
    Crossfade(current = uiModels) {
        when (uiModels) {
            is IdleUIModel -> RenderIdle()
            is LoadingUIModel -> RenderLoading(source = uiModels.source)
            is SuccessUIModel -> RenderArticles(articles = uiModels.articlesUIModel.articles, onNewsClicked)
            is ErrorUIModel -> RenderError(errorUIModel = uiModels)
        }
    }
}

@Composable
fun RenderIdle() {
    Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
        Column {
            CircularProgressIndicator(modifier = Modifier.gravity(CenterHorizontally))
            Text(
                text = stringResource(id = R.string.idle_text),
                style = typography.h6,
                color = colors.onSurface,
                modifier = Modifier.wrapContentWidth(CenterHorizontally)
            )
        }
    }
}

@Composable
fun RenderLoading(source: Source) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), gravity = ContentGravity.Center) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier.gravity(CenterHorizontally),
                color = colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.loading_text),
                style = typography.h6,
                color = colors.onSurface,
                textAlign = Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = source.title,
                style = typography.h3,
                color = colors.onSurface,
                textAlign = Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RenderArticles(articles: List<ArticleUIModel>, onNewsClicked: (String) -> Unit) {
    LazyColumnFor(
        items = articles,
        modifier = Modifier.weight(1f, true).padding(start = 16.dp, end = 16.dp)
    ) {
        RenderArticle(article = it, onNewsClicked)
    }
    Spacer(modifier = Modifier.fillMaxWidth().height(72.dp))
}

@Composable
fun RenderArticle(article: ArticleUIModel, onNewsClicked: (String) -> Unit) {
    Spacer(modifier = Modifier.wrapContentWidth().wrapContentHeight().preferredSize(8.dp))
    Card(
        shape = shapes.medium,
        elevation = 8.dp,
        contentColor = colors.surface,
        modifier = Modifier.fillMaxWidth().clickable(
            enabled = true,
            indication = RippleIndication(bounded = true),
            onClick = { onNewsClicked.invoke(article.url) }
        )
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
                        style = typography.h6,
                        color = colors.onSurface,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(
                        modifier = Modifier.wrapContentWidth().wrapContentHeight()
                            .preferredSize(4.dp)
                    )
                    Text(
                        text = article.description,
                        style = typography.body1,
                        color = colors.onSurface,
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
                style = typography.body1,
                color = colors.error,
                textAlign = Center,
                modifier = Modifier.fillMaxWidth().gravity(align = CenterHorizontally)
            )
            Spacer(modifier = Modifier.preferredHeight(4.dp))
            Text(
                text = errorUIModel.source.title,
                style = typography.h3,
                color = colors.error,
                textAlign = Center,
                modifier = Modifier.fillMaxWidth().gravity(align = CenterHorizontally)
            )
            Spacer(modifier = Modifier.preferredHeight(8.dp))
            Text(
                text = stringResource(
                    id = R.string.error_due_to,
                    formatArgs = arrayOf(errorUIModel.exception.localizedMessage)
                ),
                style = typography.h5,
                color = colors.error,
                textAlign = Center,
                modifier = Modifier.fillMaxWidth().gravity(align = CenterHorizontally)
            )
        }
    }
}

@Composable
fun NewsBottomAppBar(sources: List<Source>, onSourceSelected: (Source) -> Unit) {
    if (ConfigurationAmbient.current.orientation == ORIENTATION_PORTRAIT) {
        BottomAppBar(modifier = Modifier.wrapContentHeight(align = CenterVertically)) {
            LazyRowFor(
                items = sources,
                modifier = Modifier.fillMaxHeight()
            ) {
                BottomAppBarItem(
                    source = it,
                    onSourceSelected = onSourceSelected
                )
            }
        }
    }
}

@Composable
fun BottomAppBarItem(source: Source, onSourceSelected: (Source) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .clickable(
                enabled = true,
                indication = RippleIndication(bounded = true),
                onClick = { onSourceSelected.invoke(source) }
            )
    ) {
        if (source.selected) {
            Box(
                modifier = Modifier.width(32.dp).height(2.dp)
                    .gravity(align = CenterHorizontally),
                shape = shapes.medium,
                backgroundColor = colors.onPrimary
            )
        }
        Text(
            text = source.title,
            style = if (source.selected) typography.h5 else typography.h6,
            color = colors.onPrimary,
            textAlign = Center,
            textDecoration = if (source.selected) TextDecoration.None else TextDecoration.LineThrough,
            modifier = Modifier.wrapContentSize().padding(8.dp)
        )
        if (source.selected) {
            Box(
                modifier = Modifier.width(64.dp).height(2.dp)
                    .gravity(align = CenterHorizontally),
                shape = shapes.medium,
                backgroundColor = colors.onPrimary
            )
        }
    }
}

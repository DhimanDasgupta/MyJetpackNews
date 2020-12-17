package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.AmbientIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
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
import com.dhimandasgupta.myjetpacknews.viewmodel.MainActivityViewModel
import com.microsoft.device.dualscreen.core.ScreenHelper
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalAnimationApi
@Composable
fun SingleSourceScreen(
    viewModel: MainActivityViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    MyNewsTheme {
        ThemedSingleSourceScreen(
            viewModel = viewModel,
            onUpClicked = onUpClicked,
            onNewsClicked = onNewsClicked
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun ThemedSingleSourceScreen(
    viewModel: MainActivityViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    val newsUiState = viewModel
        .newsUiState
        .observeAsState(initial = initialNewsUiState)

    Scaffold(
        topBar = {
            NewsTopAppBar(
                source = newsUiState.value.currentSource,
                onUpClicked = onUpClicked
            )
        },
        bodyContent = {
            NewsBody(
                uiModels = newsUiState.value.uiModels,
                sources = newsUiState.value.allSources,
                onNewsClicked = onNewsClicked
            ) { source ->
                if (!source.selected) viewModel.fetchNewsFromSource(
                    source = source
                )
            }
        },
        bottomBar = {
            NewsBottomAppBar(
                sources = newsUiState.value.allSources
            ) { source ->
                if (!source.selected) viewModel.fetchNewsFromSource(
                    source = source
                )
            }
        },
    )
}

@Composable
fun NewsTopAppBar(
    source: Source,
    onUpClicked: () -> Unit
) {
    val isDualScreenMode = ScreenHelper.isDualMode(AmbientContext.current)

    TopAppBar {
        IconButton(
            onClick = { onUpClicked.invoke() },
            modifier = Modifier
                .wrapContentSize(align = Center)
                .align(CenterVertically),
        ) {
            Icon(
                imageVector = vectorResource(R.drawable.ic_arrow_back),
                tint = colors.onPrimary,
                modifier = Modifier.preferredSize(48.dp)
            )
        }

        Text(
            text = stringResource(
                id = R.string.news_from,
                formatArgs = arrayOf(source.title)
            ),
            style = typography.h5,
            color = colors.onPrimary,
            textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterVertically)
                .padding(8.dp),
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsBody(
    uiModels: UIModels,
    sources: List<Source>,
    onNewsClicked: (String) -> Unit,
    onSourceSelected: (Source) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = colors.surface),
        contentAlignment = TopStart
    ) {
        val isLandscape = AmbientConfiguration.current.orientation == ORIENTATION_LANDSCAPE
        val isDualScreenMode = ScreenHelper.isDualMode(AmbientContext.current)
        val leftSourcesWeight = when {
            isLandscape && isDualScreenMode -> 0.5f
            isLandscape -> 0.3f
            else -> 0.0f
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLandscape) {
                LazyColumn(
                    modifier = Modifier.weight(
                        leftSourcesWeight, true
                    ).fillMaxHeight().padding(start = 8.dp)
                ) {
                    items(
                        items = sources,
                        itemContent = {
                            BottomAppBarItem(
                                source = it,
                                onSourceSelected = onSourceSelected
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f - leftSourcesWeight, true)
            ) {
                NewsContainer(uiModels = uiModels, onNewsClicked)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsContainer(
    uiModels: UIModels,
    onNewsClicked: (String) -> Unit
) {
    Crossfade(
        current = uiModels
    ) {
        when (uiModels) {
            is IdleUIModel -> RenderIdle()
            is LoadingUIModel -> RenderLoading(
                source = uiModels.source
            )
            is SuccessUIModel -> RenderArticles(
                articles = uiModels.articlesUIModel.articles, onNewsClicked
            )
            is ErrorUIModel -> RenderError(
                errorUIModel = uiModels
            )
        }
    }
}

@Composable
fun RenderIdle() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Center
    ) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier.align(CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.idle_text),
                style = typography.h6,
                color = colors.onSurface,
                modifier = Modifier
                    .wrapContentWidth(CenterHorizontally)
            )
        }
    }
}

@Composable
fun RenderLoading(
    source: Source
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Center
    ) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(CenterHorizontally),
                color = colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.loading_text),
                style = typography.h6,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = source.title,
                style = typography.h3,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RenderArticles(
    articles: List<ArticleUIModel>,
    onNewsClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1f)
    ) {
        items(
            items = articles,
            itemContent = {
                RenderArticle(
                    article = it,
                    onNewsClicked = onNewsClicked
                )
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun RenderArticle(
    article: ArticleUIModel,
    onNewsClicked: (String) -> Unit
) {
    AnimatedVisibility(
        initiallyVisible = false,
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Card(
                shape = shapes.medium,
                elevation = 8.dp,
                contentColor = colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = true,
                        indication = AmbientIndication.current(),
                        onClick = { onNewsClicked.invoke(article.url) }
                    )
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .preferredSize(8.dp)
                    )
                    Row {
                        Spacer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .preferredSize(8.dp)
                        )
                        CoilImage(
                            data = article.imageUrl,
                            modifier = Modifier
                                .preferredSize(100.dp),
                            fadeIn = true,
                        )
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(
                                text = article.title,
                                style = typography.h6,
                                color = colors.onSurface,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .preferredSize(4.dp)
                            )
                            Text(
                                text = article.description,
                                style = typography.body1,
                                color = colors.onSurface,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .preferredSize(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RenderError(
    errorUIModel: ErrorUIModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Center
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.error_text),
                style = typography.body1,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = CenterHorizontally)
            )

            Spacer(
                modifier = Modifier
                    .preferredHeight(4.dp)
            )
            Text(
                text = errorUIModel.source.title,
                style = typography.h3,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = CenterHorizontally)
            )
            Spacer(
                modifier = Modifier
                    .preferredHeight(8.dp)
            )
            Text(
                text = stringResource(
                    id = R.string.error_due_to,
                    formatArgs = arrayOf(errorUIModel.exception.localizedMessage)
                ),
                style = typography.h5,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = CenterHorizontally)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsBottomAppBar(sources: List<Source>, onSourceSelected: (Source) -> Unit) {
    if (AmbientConfiguration.current.orientation == ORIENTATION_PORTRAIT) {
        BottomAppBar(
            modifier = Modifier
                .wrapContentHeight(align = CenterVertically)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(
                    items = sources,
                    itemContent = {
                        BottomAppBarItem(
                            source = it,
                            onSourceSelected = onSourceSelected
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomAppBarItem(
    source: Source,
    onSourceSelected: (Source) -> Unit
) {
    val isLandscape = AmbientConfiguration.current.orientation == ORIENTATION_LANDSCAPE
    val textColor = if (isLandscape) colors.onSurface else colors.onPrimary

    val isDualScreenMode = ScreenHelper.isDualMode(AmbientContext.current)

    AnimatedVisibility(
        initiallyVisible = false,
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    enabled = true,
                    indication = AmbientIndication.current(),
                    onClick = { onSourceSelected.invoke(source) }
                )
        ) {
            if (source.selected) {
                Box(
                    modifier = Modifier
                        .width(if (isDualScreenMode) 128.dp else 32.dp)
                        .height(2.dp)
                        .align(alignment = CenterHorizontally)
                        .background(
                            color = textColor,
                            shape = shapes.medium
                        ),
                )
            }
            Text(
                text = source.title,
                style = if (source.selected) typography.h5 else typography.h6,
                color = textColor,
                textAlign = if (isDualScreenMode) TextAlign.Center else TextAlign.Start,
                textDecoration = if (source.selected) TextDecoration.None else TextDecoration.LineThrough,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            if (source.selected) {
                Box(
                    modifier = Modifier
                        .width(if (isDualScreenMode) 156.dp else 32.dp)
                        .height(2.dp)
                        .align(alignment = CenterHorizontally)
                        .background(
                            color = textColor,
                            shape = shapes.medium
                        ),
                )
            }
        }
    }
}

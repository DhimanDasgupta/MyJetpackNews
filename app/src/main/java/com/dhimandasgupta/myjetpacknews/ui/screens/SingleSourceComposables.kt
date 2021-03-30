@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceViewModel
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.microsoft.device.dualscreen.core.ScreenHelper

@ExperimentalAnimationApi
@Composable
fun SingleSourceScreen(
    viewModel: SingleSourceViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    MyNewsTheme {
        ProvideWindowInsets {
            ThemedSingleSourceScreen(
                viewModel = viewModel,
                onUpClicked = onUpClicked,
                onNewsClicked = onNewsClicked
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun ThemedSingleSourceScreen(
    viewModel: SingleSourceViewModel,
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
        content = {
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
private fun NewsTopAppBar(
    source: Source,
    onUpClicked: () -> Unit
) {
    val isDualScreenMode = ScreenHelper.isDualMode(LocalContext.current)

    Column {
        Spacer(
            modifier = Modifier
                .background(colors.primary)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )

        TopAppBar {
            IconButton(
                onClick = { onUpClicked.invoke() },
                modifier = Modifier
                    .wrapContentSize(align = Center)
                    .align(CenterVertically),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = colors.onPrimary,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Back"
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
}

@ExperimentalAnimationApi
@Composable
private fun NewsBody(
    uiModels: UIModels,
    sources: List<Source>,
    onNewsClicked: (String) -> Unit,
    onSourceSelected: (Source) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = colors.surface)
            .navigationBarsPadding(),
        contentAlignment = TopStart
    ) {
        val isLandscape = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE
        val isDualScreenMode = ScreenHelper.isDualMode(LocalContext.current)
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
                    modifier = Modifier
                        .weight(
                            leftSourcesWeight, true
                        )
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    items(
                        count = sources.size,
                        itemContent = { index ->
                            BottomAppBarItem(
                                source = sources[index],
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
private fun NewsContainer(
    uiModels: UIModels,
    onNewsClicked: (String) -> Unit
) {
    Crossfade(
        targetState = uiModels
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
            else -> {
            }
        }
    }
}

@Composable
private fun RenderIdle() {
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
private fun RenderLoading(
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
                modifier = Modifier.align(CenterHorizontally),
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
private fun RenderArticles(
    articles: List<ArticleUIModel>,
    onNewsClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(1f)
    ) {
        items(
            count = articles.size,
            itemContent = { index ->
                RenderArticle(
                    article = articles[index],
                    onNewsClicked = onNewsClicked
                )
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun RenderArticle(
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
                        onClick = { onNewsClicked.invoke(article.url) }
                    )
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .size(8.dp)
                    )
                    Row {
                        Spacer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .size(8.dp)
                        )
                        CoilImage(
                            data = article.imageUrl,
                            modifier = Modifier.size(100.dp),
                            fadeIn = true,
                            contentDescription = article.description
                        )
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = article.title,
                                style = typography.h6,
                                color = colors.onSurface,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .size(4.dp)
                            )
                            Text(
                                text = article.description,
                                style = typography.body1,
                                color = colors.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .size(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderError(
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
                modifier = Modifier.size(4.dp)
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
                modifier = Modifier.size(8.dp)
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
private fun NewsBottomAppBar(
    sources: List<Source>,
    onSourceSelected: (Source) -> Unit
) {
    if (LocalConfiguration.current.orientation == ORIENTATION_PORTRAIT) {
        val insets = LocalWindowInsets.current

        BottomAppBar(
            modifier = Modifier
                .wrapContentHeight(align = CenterVertically)
                .navigationBarsPadding()
        ) {
            Column {
                LazyRow(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(
                        count = sources.size,
                        itemContent = { index ->
                            BottomAppBarItem(
                                source = sources[index],
                                onSourceSelected = onSourceSelected
                            )
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(bottom = insets.navigationBars.bottom.dp)
                        .background(colors.primary.copy(alpha = 0.7f))
                        .navigationBarsHeight() // Match the height of the navigation bar
                        .fillMaxWidth()
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun BottomAppBarItem(
    source: Source,
    onSourceSelected: (Source) -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE
    val textColor = if (isLandscape) colors.onSurface else colors.onPrimary

    val isDualScreenMode = ScreenHelper.isDualMode(LocalContext.current)

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

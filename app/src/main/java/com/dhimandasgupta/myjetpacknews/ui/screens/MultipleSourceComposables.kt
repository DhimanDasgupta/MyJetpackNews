package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.data.presentaion.ArticleUIModel
import com.dhimandasgupta.data.presentaion.ArticlesUIModel
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.IdleUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.data.presentaion.UIModels
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.common.shapes
import com.dhimandasgupta.myjetpacknews.viewmodel.MainActivityViewModel
import com.microsoft.device.dualscreen.core.ScreenHelper
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalAnimationApi
@Composable
fun MultipleSourceScreen(
    viewModel: MainActivityViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    MyNewsTheme {
        ThemedMultiSourceScreen(
            viewModel = viewModel,
            onUpClicked = onUpClicked,
            onNewsClicked = onNewsClicked
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun ThemedMultiSourceScreen(
    viewModel: MainActivityViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    Scaffold(
        topBar = {
            NewsTopAppBarForMultiSource(
                onUpClicked = onUpClicked
            )
        },
        bodyContent = {
            NewsBodyForMultiSource(
                mainActivityViewModel = viewModel,
                onNewsClicked = onNewsClicked
            )
        }
    )
}

@Composable
fun NewsTopAppBarForMultiSource(
    onUpClicked: () -> Unit
) {
    val isDualScreenMode = ScreenHelper.isDualMode(ContextAmbient.current)

    TopAppBar {
        IconButton(
            onClick = { onUpClicked.invoke() },
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .align(alignment = Alignment.CenterVertically),
            icon = {
                Icon(
                    asset = vectorResource(R.drawable.ic_arrow_back),
                    tint = colors.onPrimary,
                    modifier = Modifier
                        .preferredSize(48.dp)
                )
            }
        )
        Text(
            text = stringResource(id = R.string.multi_source_title),
            style = typography.h5,
            color = colors.onPrimary,
            textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterVertically)
                .padding(8.dp)
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsBodyForMultiSource(
    mainActivityViewModel: MainActivityViewModel,
    onNewsClicked: (String) -> Unit
) {
    val newsUiState = mainActivityViewModel
        .sourcesLiveData
        .observeAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.surface),
        alignment = Alignment.TopStart
    ) {
        LazyColumnFor(
            items = newsUiState.value,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            NewsRowForSource(
                mainActivityViewModel = mainActivityViewModel,
                source = it,
                onNewsClicked = onNewsClicked
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsRowForSource(
    mainActivityViewModel: MainActivityViewModel,
    source: Source,
    onNewsClicked: (String) -> Unit
) {
    val isDualScreenMode = ScreenHelper.isDualMode(ContextAmbient.current)

    Column {
        val news: MutableState<UIModels> = remember { mutableStateOf(LoadingUIModel(source)) }

        LaunchedEffect(
            subject = source,
            block = {
                news.value = mainActivityViewModel.fetchNewsFrom(source)
            }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        Text(
            text = source.title,
            style = typography.h5,
            color = colors.onSurface,
            textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )

        when (news.value) {
            is IdleUIModel -> RenderNewsIdleState()
            is LoadingUIModel -> RenderLoadingState(
                source = source
            )
            is SuccessUIModel -> RenderArticlesState(
                articlesUIModel = (news.value as SuccessUIModel).articlesUIModel,
                onNewsClicked = onNewsClicked
            )
            is ErrorUIModel -> RenderErrorState(
                errorUIModel = news.value as ErrorUIModel
            )
        }
    }
}

@Composable
fun RenderNewsIdleState() {
    ListItem {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        )
    }
}

@Composable
fun RenderLoadingState(
    source: Source
) {
    val isDualScreenMode = ScreenHelper.isDualMode(ContextAmbient.current)

    ListItem {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            alignment = Alignment.Center
        ) {
            Column {
                LinearProgressIndicator(
                    modifier = Modifier.align(if (isDualScreenMode) Alignment.Start else Alignment.CenterHorizontally)
                        .padding(8.dp),
                    color = colors.onSurface
                )
                Text(
                    text = stringResource(id = R.string.loading_text),
                    style = typography.h6,
                    color = colors.onSurface,
                    textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = source.title,
                    style = typography.h3,
                    color = colors.onSurface,
                    textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RenderArticlesState(
    articlesUIModel: ArticlesUIModel,
    onNewsClicked: (String) -> Unit
) {
    ListItem {
        LazyRowFor(
            items = articlesUIModel.articles,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            RenderEachArticle(
                article = it,
                onNewsClicked = onNewsClicked
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RenderEachArticle(
    article: ArticleUIModel,
    onNewsClicked: (String) -> Unit
) {
    val cardSize = 260.dp

    AnimatedVisibility(
        initiallyVisible = false,
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
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
                    .size(cardSize)
                    .clickable(
                        enabled = true,
                        indication = RippleIndication(bounded = true),
                        onClick = {
                            onNewsClicked.invoke(article.url)
                        }
                    )
            ) {
                Modifier.fillMaxSize()
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CoilImage(
                        data = article.imageUrl,
                        modifier = Modifier.size(cardSize),
                        contentScale = ContentScale.Crop,
                        fadeIn = true,
                    )
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .width(cardSize)
                            .background(
                                color = colors.surface.copy(alpha = 0.6f),
                                shape = shapes.medium.copy(topLeft = CornerSize(0.dp), topRight = CornerSize(0.dp)),
                            )
                    ) {
                        Text(
                            text = article.title,
                            style = typography.h6,
                            color = colors.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RenderErrorState(
    errorUIModel: ErrorUIModel
) {
    val isDualScreenMode = ScreenHelper.isDualMode(ContextAmbient.current)

    ListItem {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            alignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.error_text),
                    style = typography.body1,
                    color = colors.error,
                    textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(
                    modifier = Modifier.preferredHeight(4.dp)
                )
                Text(
                    text = errorUIModel.source.title,
                    style = typography.h3,
                    color = colors.error,
                    textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(
                    modifier = Modifier.preferredHeight(8.dp)
                )
                Text(
                    text = stringResource(
                        id = R.string.error_due_to,
                        formatArgs = arrayOf(errorUIModel.exception.localizedMessage)
                    ),
                    style = typography.h5,
                    color = colors.onSurface,
                    textAlign = if (isDualScreenMode) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}

package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.data.presentaion.ArticlesUIModel
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.IdleUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.NewsUiState
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.data.presentaion.initialNewsUiState
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.KenBurns
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.common.shapes
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceStoryViewModel
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun SingleSourceStoryScreen(
    viewModel: SingleSourceStoryViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    MyNewsTheme {
        ProvideWindowInsets {
            ThemedSingleSourceStoryScreen(
                viewModel = viewModel,
                onUpClicked = onUpClicked,
                onNewsClicked = onNewsClicked
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun ThemedSingleSourceStoryScreen(
    viewModel: SingleSourceStoryViewModel,
    onUpClicked: () -> Unit,
    onNewsClicked: (String) -> Unit
) {
    val newsUiState = viewModel
        .newsUiState
        .observeAsState(initial = initialNewsUiState)

    Scaffold(
        bodyContent = {
            Box(modifier = Modifier.fillMaxSize()) {
                StoryBody(newsUiState = newsUiState)
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
private fun StoryBody(
    newsUiState: State<NewsUiState>
) {
    Crossfade(
        current = newsUiState.value
    ) {
        when (newsUiState.value.uiModels) {
            is IdleUIModel -> RenderIdle()
            is LoadingUIModel -> RenderLoading(
                source = (newsUiState.value.uiModels as LoadingUIModel).source
            )
            is SuccessUIModel -> RenderSuccess(
                articlesUIModel = (newsUiState.value.uiModels as SuccessUIModel).articlesUIModel,
            )
            is ErrorUIModel -> RenderError(
                errorUIModel = (newsUiState.value.uiModels as ErrorUIModel)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun RenderSuccess(
    articlesUIModel: ArticlesUIModel
) {
    val scope = rememberCoroutineScope()
    val state = remember { mutableStateOf(0) }

    DisposableEffect(
        key1 = articlesUIModel,
        effect = {
            scope.launch {
                while (isActive) {
                    delay(5000)
                    state.value = if (state.value >= articlesUIModel.articles.size - 1) 0 else ++state.value
                }
            }

            onDispose { scope.cancel(null) }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Full Screen Image
        KenBurns(
            data = articlesUIModel.articles[state.value].imageUrl,
            modifier = Modifier.fillMaxSize()
        )

        // TOp Semi transparent Status Bar
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .background(
                    color = colors.surface.copy(alpha = 0.6f),
                    shape = shapes.medium.copy(
                        topLeft = CornerSize(0.dp),
                        topRight = CornerSize(0.dp)
                    ),
                )
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Bottom Text with Semi transparent navigation bar
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = colors.surface.copy(alpha = 0.6f),
                    shape = shapes.medium.copy(
                        topLeft = CornerSize(0.dp),
                        topRight = CornerSize(0.dp)
                    ),
                )
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = articlesUIModel.articles[state.value].title,
                    style = MaterialTheme.typography.h6,
                    color = colors.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp)
                )

                Text(
                    text = articlesUIModel.articles[state.value].description,
                    style = MaterialTheme.typography.body1,
                    color = colors.onSurface,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun RenderIdle() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.idle_text),
                style = MaterialTheme.typography.h6,
                color = colors.onSurface,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
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
        contentAlignment = Alignment.Center
    ) {
        Column {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.loading_text),
                style = MaterialTheme.typography.h6,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = source.title,
                style = MaterialTheme.typography.h3,
                color = colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
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
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.error_text),
                style = MaterialTheme.typography.body1,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(
                modifier = Modifier
                    .preferredHeight(4.dp)
            )
            Text(
                text = errorUIModel.source.title,
                style = MaterialTheme.typography.h3,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
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
                style = MaterialTheme.typography.h5,
                color = colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

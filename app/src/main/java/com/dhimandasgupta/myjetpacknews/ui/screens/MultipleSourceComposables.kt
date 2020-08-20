package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Stack
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.launchInComposition
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.state
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.dhimandasgupta.myjetpacknews.viewmodel.MultipleSourceViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@Composable
fun MultipleSourceScreen(multipleSourceViewModel: MultipleSourceViewModel, onUpClicked: () -> Unit) {
    MyNewsTheme {
        ThemedMultiSourceScreen(
            multipleSourceViewModel = multipleSourceViewModel,
            onUpClicked = onUpClicked
        )
    }
}

@Composable
fun ThemedMultiSourceScreen(multipleSourceViewModel: MultipleSourceViewModel, onUpClicked: () -> Unit) {
    Scaffold(
        topBar = { NewsTopAppBarForMultiSource(onUpClicked = onUpClicked) },
        bodyContent = { NewsBodyForMultiSource(multipleSourceViewModel = multipleSourceViewModel) }
    )
}

@Composable
fun NewsTopAppBarForMultiSource(onUpClicked: () -> Unit) {
    TopAppBar {
        IconButton(
            onClick = { onUpClicked.invoke() },
            modifier = Modifier.wrapContentSize(align = Alignment.Center)
                .gravity(align = Alignment.CenterVertically),
            icon = {
                Icon(
                    asset = vectorResource(id = R.drawable.ic_arrow_back),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        )
        Text(
            text = stringResource(id = R.string.multi_source_title),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
        )
    }
}

@Composable
fun NewsBodyForMultiSource(multipleSourceViewModel: MultipleSourceViewModel) {
    val newsUiState = multipleSourceViewModel.sourcesLiveData.observeAsState(initial = emptyList())

    Box(
        Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        LazyColumnFor(
            items = newsUiState.value,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            NewsRowForSource(
                multipleSourceViewModel = multipleSourceViewModel,
                source = it
            )
        }
    }
}

@Composable
fun NewsRowForSource(multipleSourceViewModel: MultipleSourceViewModel, source: Source) {
    Column {
        var news by state<UIModels> { LoadingUIModel(source) }

        launchInComposition(
            block = {
                news = multipleSourceViewModel.fetchNewsFrom(source)
            }
        )
        Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))
        Text(
            text = source.title,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth().wrapContentSize(align = Alignment.Center)
        )
        Spacer(modifier = Modifier.fillMaxWidth().height(4.dp))

        when (news) {
            is IdleUIModel -> RenderNewsIdleState()
            is LoadingUIModel -> RenderLoadingState(source = source)
            is SuccessUIModel -> RenderArticlesState(articlesUIModel = (news as SuccessUIModel).articlesUIModel)
            is ErrorUIModel -> RenderErrorState(errorUIModel = news as ErrorUIModel)
        }
    }
}

@Composable
fun RenderNewsIdleState() {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight())
}

@Composable
fun RenderLoadingState(source: Source) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), gravity = ContentGravity.Center) {
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
fun RenderArticlesState(articlesUIModel: ArticlesUIModel) {
    ScrollableRow(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        articlesUIModel.articles.map {
            RenderEachArticle(article = it)
        }
    }
}

@Composable
fun RenderEachArticle(article: ArticleUIModel) {
    val cardSize = 260.dp

    Spacer(modifier = Modifier.fillMaxSize().preferredSize(16.dp))
    Card(
        shape = shapes.medium,
        elevation = 8.dp,
        color = MaterialTheme.colors.surface,
        modifier = Modifier.size(cardSize).clickable(
            enabled = true,
            indication = RippleIndication(bounded = true),
            onClick = {}
        )
    ) {
        Stack(modifier = Modifier.fillMaxSize()) {
            CoilImageWithCrossfade(
                data = article.urlToImage,
                modifier = Modifier.size(cardSize),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.gravity(Alignment.BottomCenter).width(cardSize),
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.6f),
                shape = shapes.medium.copy(topLeft = CornerSize(0.dp), topRight = CornerSize(0.dp))
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun RenderErrorState(errorUIModel: ErrorUIModel) {
    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp), gravity = ContentGravity.Center) {
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
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().gravity(align = Alignment.CenterHorizontally)
            )
        }
    }
}

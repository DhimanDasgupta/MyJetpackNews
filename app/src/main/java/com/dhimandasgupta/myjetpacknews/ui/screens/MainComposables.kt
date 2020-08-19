package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.data.Page
import com.dhimandasgupta.myjetpacknews.ui.ext.toListOfPairedPages

@Composable
fun MainScreen(pages: List<Page>) {
    MyNewsTheme {
        Scaffold(
            topBar = { MainTopAppBar() },
            bodyContent = { MainContent(pages) }
        )
    }
}

@Composable
fun MainTopAppBar() {
    TopAppBar {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
        )
    }
}

@Composable
fun MainContent(pages: List<Page>) {
    when (ConfigurationAmbient.current.orientation) {
        ORIENTATION_LANDSCAPE -> RenderLandscapeContent(pages = pages)
        else -> RenderPortraitContent(pages = pages)
    }
}

@Composable
fun RenderLandscapeContent(pages: List<Page>) {
    val context = ContextAmbient.current

    LazyColumnForIndexed(items = pages.toListOfPairedPages()) { _, item ->
        Row {
            RenderContent(
                page = item.first,
                onClick = { context.startActivity(Intent(context, item.first.clazz)) }
            )
            item.second?.let {
                RenderContent(
                    page = it,
                    onClick = { context.startActivity(Intent(context, it.clazz)) }
                )
            }
        }
    }
}

@Composable
fun RenderPortraitContent(pages: List<Page>) {
    val context = ContextAmbient.current

    LazyColumnForIndexed(items = pages) { _, item ->
        RenderContent(
            page = item,
            onClick = { context.startActivity(Intent(context, item.clazz)) }
        )
    }
}

@Composable
fun RenderContent(page: Page, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().weight(1f, true).gravity(Alignment.CenterHorizontally).padding(8.dp).clickable(
            enabled = true,
            indication = RippleIndication(),
            onClick = { onClick.invoke() }
        ),
        gravity = ContentGravity.Center
    ) {
        Text(
            text = page.name,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

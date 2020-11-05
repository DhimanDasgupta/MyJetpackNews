package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign.Center
import androidx.compose.ui.text.style.TextAlign.Start
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.data.Page
import com.dhimandasgupta.myjetpacknews.ui.ext.toListOfPairedPages
import com.microsoft.device.dualscreen.core.ScreenHelper

@Composable
fun MainScreen(
    pages: List<Page>
) {
    MyNewsTheme {
        Scaffold(
            topBar = { MainTopAppBar() },
            bodyContent = { MainContent(pages) }
        )
    }
}

@Composable
fun MainTopAppBar() {
    val isDualScreenMode = ScreenHelper.isDualMode(ContextAmbient.current)

    TopAppBar {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onPrimary,
            textAlign = if (isDualScreenMode) Start else Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(8.dp)
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
fun RenderLandscapeContent(
    pages: List<Page>
) {
    val context = ContextAmbient.current

    LazyColumnForIndexed(
        items = pages.toListOfPairedPages(),
        modifier = Modifier.fillMaxSize(1.0f)
    ) { _, item ->
        Row {
            RenderContent(
                page = item.first,
                onClick = { context.startActivity(Intent(context, item.first.clazz)) },
                modifier = Modifier.fillMaxWidth(0.5f),
            )
            item.second?.let {
                RenderContent(
                    page = it,
                    onClick = { context.startActivity(Intent(context, it.clazz)) },
                    modifier = Modifier.fillMaxWidth(1.0f),
                )
            }
        }
    }
}

@Composable
fun RenderPortraitContent(
    pages: List<Page>
) {
    val context = ContextAmbient.current

    LazyColumnForIndexed(items = pages) { _, item ->
        RenderContent(
            page = item,
            onClick = { context.startActivity(Intent(context, item.clazz)) },
            modifier = Modifier.fillMaxWidth(1.0f),
        )
    }
}

@Composable
fun RenderContent(page: Page, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(8.dp)
            .clickable(
                enabled = true,
                indication = RippleIndication(),
                onClick = { onClick.invoke() }
            ),
        alignment = Alignment.Center,
        children = {
            Text(
                text = page.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                textAlign = Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

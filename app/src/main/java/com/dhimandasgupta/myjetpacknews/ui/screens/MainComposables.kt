package com.dhimandasgupta.myjetpacknews.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.AmbientIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.AmbientContext
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
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun MainScreen(
    navigateToPage: (Page) -> Unit,
    pages: List<Page>
) {
    MyNewsTheme {
        ProvideWindowInsets {
            Scaffold(
                topBar = { MainTopAppBar() },
                bodyContent = {
                    MainContent(
                        navigateToPage = navigateToPage,
                        pages = pages
                    )
                },
                bottomBar = { RenderBottomBar() }
            )
        }
    }
}

@Composable
private fun MainTopAppBar() {
    val isDualScreenMode = ScreenHelper.isDualMode(AmbientContext.current)

    Column {
        Spacer(
            modifier = Modifier
                .background(colors.primary)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )

        TopAppBar {
            Text(
                text = stringResource(id = R.string.app_name),
                style = typography.h5,
                color = colors.onPrimary,
                textAlign = if (isDualScreenMode) Start else Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun MainContent(
    navigateToPage: (Page) -> Unit,
    pages: List<Page>
) {
    when (AmbientConfiguration.current.orientation) {
        ORIENTATION_LANDSCAPE -> RenderLandscapeContent(
            navigateToPage = navigateToPage,
            pages = pages,
        )
        else -> RenderPortraitContent(
            navigateToPage = navigateToPage,
            pages = pages,
        )
    }
}

@Composable
private fun RenderLandscapeContent(
    navigateToPage: (Page) -> Unit,
    pages: List<Page>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(1.0f)
    ) {
        itemsIndexed(
            items = pages.toListOfPairedPages(),
            itemContent = { _, item ->
                Row {
                    RenderContent(
                        page = item.first,
                        onClick = { navigateToPage.invoke(item.first) },
                        modifier = Modifier.fillMaxWidth(0.5f),
                    )
                    item.second?.let {
                        RenderContent(
                            page = it,
                            onClick = { navigateToPage.invoke(it) },
                            modifier = Modifier.fillMaxWidth(1.0f),
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun RenderPortraitContent(
    navigateToPage: (Page) -> Unit,
    pages: List<Page>
) {
    LazyColumn {
        itemsIndexed(
            items = pages,
            itemContent = { _, item ->
                RenderContent(
                    page = item,
                    onClick = { navigateToPage.invoke(item) },
                    modifier = Modifier.fillMaxWidth(1.0f),
                )
            }
        )
    }
}

@Composable
private fun RenderContent(page: Page, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(8.dp)
            .clickable(
                enabled = true,
                indication = AmbientIndication.current(),
                onClick = { onClick.invoke() }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = page.name,
            style = typography.h6,
            color = colors.onSurface,
            textAlign = Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun RenderBottomBar() {
    val scope = rememberCoroutineScope()
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss a z") }
    val state = remember { mutableStateOf(ZonedDateTime.now().format(formatter)) }

    onActive(
        callback = {
            scope.launch {
                while (isActive) {
                    delay(1000)
                    state.value = ZonedDateTime.now().format(formatter)
                }
            }
        }
    )

    Column {
        Text(
            text = state.value,
            style = typography.h6,
            color = colors.onSurface,
            textAlign = Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        Spacer(
            modifier = Modifier
                .background(colors.primary.copy(alpha = 0.7f))
                .navigationBarsHeight() // Match the height of the navigation bar
                .fillMaxWidth()
        )
    }
}

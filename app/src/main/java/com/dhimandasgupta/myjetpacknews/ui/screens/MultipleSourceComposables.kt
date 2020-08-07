package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.myjetpacknews.R
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme

@Composable
fun MultipleSourceScreen() {
    MyNewsTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
                    )
                }
            },
            bodyContent = {
                Text(
                    text = "ToDo....",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth().gravity(Alignment.CenterHorizontally).padding(8.dp)
                )
            }
        )
    }
}

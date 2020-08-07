package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dhimandasgupta.data.presentaion.ArticleUIModel

@Composable
fun Details(articleUIModel: ArticleUIModel) {
    Scaffold {
        Column {
            Text(
                text = "News from ${articleUIModel.title}",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
            )
        }
    }
}

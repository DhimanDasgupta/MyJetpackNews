package com.dhimandasgupta.myjetpacknews.ui.main

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.wrapContentSize
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.text.style.TextAlign
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
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .wrapContentSize(align = Alignment.Center)
            )
        }
    }
}
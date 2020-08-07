package com.dhimandasgupta.myjetpacknews.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Box
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dhimandasgupta.myjetpacknews.R

@Composable
fun ColumnWithPages(activitiesToLaunch: List<Class<out Activity>>) {
    ScrollableColumn {
        TopAppBar {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center)
            )
        }

        Box(
            Modifier.fillMaxSize()
        ) {
            val context = ContextAmbient.current

            Text(
                text = "Single Sources at a time",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth().gravity(Alignment.CenterHorizontally).padding(8.dp)
                    .clickable(
                        onClick = {
                            context.startActivity(Intent(context, activitiesToLaunch[0]))
                        }
                    )
            )
            Text(
                text = "Multiple Sources at a time",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth().gravity(Alignment.CenterHorizontally).padding(8.dp)
                    .clickable(
                        onClick = {
                            context.startActivity(Intent(context, activitiesToLaunch[1]))
                        }
                    )
            )
        }
    }
}

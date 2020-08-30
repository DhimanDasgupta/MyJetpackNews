package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ext.openBrowser
import com.dhimandasgupta.myjetpacknews.ui.screens.MultipleSourceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiSourceActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultipleSourceScreen(
                onUpClicked = { finish() }
            ) { url -> openBrowser(url = url) }
        }
    }
}

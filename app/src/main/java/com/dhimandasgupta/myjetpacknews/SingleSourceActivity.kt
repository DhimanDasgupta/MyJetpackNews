package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ext.openBrowser
import com.dhimandasgupta.myjetpacknews.ui.screens.SingleSourceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleSourceActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SingleSourceScreen(
                onUpClicked = { finish() }
            ) { url -> openBrowser(url = url) }
        }
    }
}

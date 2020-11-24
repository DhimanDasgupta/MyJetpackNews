package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ui.screens.AppNavGraph
import com.dhimandasgupta.myjetpacknews.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph(
                viewModel = mainActivityViewModel
            )
        }
    }
}

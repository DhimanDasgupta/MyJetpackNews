package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import com.dhimandasgupta.myjetpacknews.ui.screens.AppNavGraph
import com.dhimandasgupta.myjetpacknews.viewmodel.MultiSourceViewModelFactory
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceStoryViewModelFactory
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var singleSourceViewModelFactory: SingleSourceViewModelFactory
    @Inject
    lateinit var multiSourceViewModelFactory: MultiSourceViewModelFactory
    @Inject
    lateinit var singleSourceStoryViewModelFactory: SingleSourceStoryViewModelFactory

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppNavGraph(
                singleSourceViewModelFactory = singleSourceViewModelFactory,
                multiSourceViewModelFactory = multiSourceViewModelFactory,
                singleSourceStoryViewModelFactory = singleSourceStoryViewModelFactory
            )
        }
    }
}

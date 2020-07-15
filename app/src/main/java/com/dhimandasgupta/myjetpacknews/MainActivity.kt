package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.dhimandasgupta.myjetpacknews.ui.main.ThemedApp
import com.dhimandasgupta.myjetpacknews.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemedApp(newsViewModel)
        }
    }
}

package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.setContent
import com.dhimandasgupta.myjetpacknews.di.Generators
import com.dhimandasgupta.myjetpacknews.ui.main.ThemedApp
import com.dhimandasgupta.myjetpacknews.viewmodel.NewsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsViewModelFactory = Generators.provideNewsViewModelFactory()
        newsViewModel = ViewModelProvider(
            viewModelStore,
            newsViewModelFactory
        ).get(NewsViewModel::class.java)

        setContent {
            ThemedApp(newsViewModel)
        }
    }
}
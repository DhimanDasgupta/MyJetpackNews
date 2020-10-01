package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ui.data.pages
import com.dhimandasgupta.myjetpacknews.ui.screens.MainScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(pages = pages)
        }
    }
}

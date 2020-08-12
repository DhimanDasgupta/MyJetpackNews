package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ui.screens.MultipleSourceScreen

class MultiSourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultipleSourceScreen(onUpClicked = { finish() })
        }
    }
}

package com.dhimandasgupta.myjetpacknews

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ui.screens.MultipleSourceScreen
import com.dhimandasgupta.myjetpacknews.viewmodel.MultipleSourceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiSourceActivity : AppCompatActivity() {
    private val multipleSourceViewModel: MultipleSourceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultipleSourceScreen(multipleSourceViewModel) {
                finish()
            }
        }
    }
}

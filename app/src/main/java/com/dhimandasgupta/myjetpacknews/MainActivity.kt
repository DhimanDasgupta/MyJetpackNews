package com.dhimandasgupta.myjetpacknews

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.myjetpacknews.ui.common.MyNewsTheme
import com.dhimandasgupta.myjetpacknews.ui.screens.ColumnWithPages
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val activitiesToLaunch: List<Class<out Activity>> = listOf(
        SingleSourceActivity::class.java,
        MultiSourceActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNewsTheme {
                ColumnWithPages(activitiesToLaunch)
            }
        }
    }
}

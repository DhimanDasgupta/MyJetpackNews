package com.dhimandasgupta.myjetpacknews.ui.data

import android.app.Activity
import com.dhimandasgupta.myjetpacknews.MultiSourceActivity
import com.dhimandasgupta.myjetpacknews.SingleSourceActivity

data class Page(
    val name: String,
    val clazz: Class<out Activity>
)

val pages = listOf(
    Page(
        name = "Single source news at a time",
        clazz = SingleSourceActivity::class.java
    ),
    Page(
        name = "Multiple Sources at a time",
        clazz = MultiSourceActivity::class.java
    ),
)

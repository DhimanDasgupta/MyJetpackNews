package com.dhimandasgupta.myjetpacknews.ui.data

import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.dhimandasgupta.myjetpacknews.ui.data.Destinations.MultipleSourceScreen
import com.dhimandasgupta.myjetpacknews.ui.data.Destinations.SingleSourceScreen
import com.dhimandasgupta.myjetpacknews.ui.data.Destinations.SingleSourceStoryScreen

data class Page(
    val name: String,
    val route: String
)

val pages = listOf(
    Page(
        name = "Single source news at a time",
        route = SingleSourceScreen
    ),
    Page(
        name = "Multiple Sources at a time",
        route = MultipleSourceScreen
    ),
    Page(
        name = "Single source with Story",
        route = SingleSourceStoryScreen
    ),
)

object Destinations {
    const val HomeScreen = "home"
    const val SingleSourceScreen = "SingleSource"
    const val MultipleSourceScreen = "MultipleSource"
    const val SingleSourceStoryScreen = "SingleSourceStory"
}

class Actions(navController: NavHostController) {
    val navigateToPage: (Page) -> Unit = { page ->
        navController.navigate(page.route)
    }
    val navigateBack: () -> Unit = {
        navController.navigateUp()
    }
}

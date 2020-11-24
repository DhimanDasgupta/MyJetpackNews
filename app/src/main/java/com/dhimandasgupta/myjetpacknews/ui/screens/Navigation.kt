package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ContextAmbient
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.myjetpacknews.ext.openBrowser
import com.dhimandasgupta.myjetpacknews.ui.data.Actions
import com.dhimandasgupta.myjetpacknews.ui.data.Destinations
import com.dhimandasgupta.myjetpacknews.ui.data.pages
import com.dhimandasgupta.myjetpacknews.viewmodel.MainActivityViewModel

@ExperimentalAnimationApi
@Composable
fun AppNavGraph(
    viewModel: MainActivityViewModel
) {
    val context = ContextAmbient.current

    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    Crossfade(current = navController.currentBackStackEntryAsState()) {
        NavHost(navController = navController, startDestination = Destinations.HomeScreen) {
            // Home Composable
            composable(route = Destinations.HomeScreen) {
                MainScreen(
                    navigateToPage = { page -> actions.navigateToPage(page) },
                    pages = pages
                )
            }

            // Single Source Composable
            composable(Destinations.SingleSourceScreen) {
                SingleSourceScreen(
                    viewModel = viewModel,
                    onUpClicked = actions.navigateBack,
                    onNewsClicked = { url -> context.openBrowser(url = url) }
                )
            }

            // Multiple Source Composable
            composable(Destinations.MultipleSourceScreen) {
                MultipleSourceScreen(
                    viewModel = viewModel,
                    onUpClicked = actions.navigateBack,
                    onNewsClicked = { url -> context.openBrowser(url = url) }
                )
            }
        }
    }
}

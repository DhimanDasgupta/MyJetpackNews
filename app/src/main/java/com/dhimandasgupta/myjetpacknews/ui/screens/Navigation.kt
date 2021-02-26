package com.dhimandasgupta.myjetpacknews.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhimandasgupta.myjetpacknews.ext.openBrowser
import com.dhimandasgupta.myjetpacknews.ui.data.Actions
import com.dhimandasgupta.myjetpacknews.ui.data.Destinations
import com.dhimandasgupta.myjetpacknews.ui.data.pages
import com.dhimandasgupta.myjetpacknews.viewmodel.MultiSourceViewModel
import com.dhimandasgupta.myjetpacknews.viewmodel.MultiSourceViewModelFactory
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceStoryViewModel
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceStoryViewModelFactory
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceViewModel
import com.dhimandasgupta.myjetpacknews.viewmodel.SingleSourceViewModelFactory

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun AppNavGraph(
    singleSourceViewModelFactory: SingleSourceViewModelFactory,
    multiSourceViewModelFactory: MultiSourceViewModelFactory,
    singleSourceStoryViewModelFactory: SingleSourceStoryViewModelFactory
) {
    val context = LocalContext.current

    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    Crossfade(targetState = navController.currentBackStackEntryAsState()) {
        NavHost(navController = navController, startDestination = Destinations.HomeScreen) {
            // Home Composable
            composable(route = Destinations.HomeScreen) {
                MainScreen(
                    navigateToPage = { page -> actions.navigateToPage(page) },
                    pages = pages
                )
            }

            // Single Source Composable
            composable(Destinations.SingleSourceScreen) { navBackStackEntry ->
                val viewModelProvider = ViewModelProvider(
                    navBackStackEntry.viewModelStore,
                    singleSourceViewModelFactory
                )
                SingleSourceScreen(
                    viewModel = viewModelProvider.get(SingleSourceViewModel::class.java),
                    onUpClicked = actions.navigateBack,
                    onNewsClicked = { url -> context.openBrowser(url = url) }
                )
            }

            // Multiple Source Composable
            composable(Destinations.MultipleSourceScreen) { navBackStackEntry ->
                val viewModelProvider = ViewModelProvider(
                    navBackStackEntry.viewModelStore,
                    multiSourceViewModelFactory
                )
                MultipleSourceScreen(
                    viewModel = viewModelProvider.get(MultiSourceViewModel::class.java),
                    onUpClicked = actions.navigateBack,
                    onNewsClicked = { url -> context.openBrowser(url = url) }
                )
            }

            // Single Source with Story
            composable(Destinations.SingleSourceStoryScreen) { navBackStackEntry ->
                val viewModelProvider = ViewModelProvider(
                    navBackStackEntry.viewModelStore,
                    singleSourceStoryViewModelFactory
                )
                SingleSourceStoryScreen(
                    viewModel = viewModelProvider.get(SingleSourceStoryViewModel::class.java),
                    onUpClicked = actions.navigateBack,
                    onNewsClicked = { url -> context.openBrowser(url = url) }
                )
            }
        }
    }
}

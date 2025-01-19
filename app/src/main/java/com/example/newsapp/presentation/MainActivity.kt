package com.example.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.bookmarks.BookmarksScreen
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.newsDetails.NewsScreenDetails
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.NavRoute
import com.example.newsapp.utils.NavScreens
import com.example.newsapp.utils.bottomNavItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                val isBottomBarVisible = remember {
                    mutableStateOf(true)
                }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = isBottomBarVisible.value
                        ) {
                            BottomAppBar {
                                val currentRoute =
                                    navController.currentBackStackEntryAsState().value?.destination?.route
                                bottomNavItems.forEach {
                                    NavigationBarItem(
                                        icon = {
                                            Image(imageVector = it.icon, contentDescription = null)
                                        },
                                        label = { Text(it.title) },
                                        selected = currentRoute == it.route,
                                        onClick = {
                                            navController.navigate(it.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {

                        NavHost(
                            navController = navController,
                            startDestination = NavScreens.Home.route
                        ) {
                            composable(NavScreens.Home.route) {
                                HomeScreen(navController)
                                isBottomBarVisible.value = true
                            }
                            composable("/details/news={news}&isLocal={isLocal}") {
                                val newsJson = it.arguments?.getString("news")
                                val isLocal = it.arguments?.getString("isLocal").toBoolean()
                                val news = NavRoute.getNewsFromRoute(newsJson!!)
                                NewsScreenDetails(navController, news, isLocal ?: false)
                                isBottomBarVisible.value = false
                            }
                            composable(NavScreens.Bookmarks.route) {
                                BookmarksScreen(navController)
                                isBottomBarVisible.value = true
                            }
                        }
                    }

                }
            }
        }
    }
}



package com.example.newsapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.bookmarks.BookmarksScreen
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.newsDetails.NewsScreenDetails
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.NavRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "/home") {
                        composable("/home") {
                            HomeScreen(navController)
                        }
                        composable("/details/news={news}") {
                            val newsJson = it.arguments?.getString("news")
                            val news = NavRoute.getNewsFromRoute(newsJson!!)
                            NewsScreenDetails(navController, news)
                        }
                        composable("/bookmarks") {
                            BookmarksScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

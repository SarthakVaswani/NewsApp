package com.example.newsapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreens(val route: String, val icon: ImageVector, val title: String) {
    data object Home : NavScreens("/home", Icons.Filled.Home, title = "Home")
    data object Bookmarks : NavScreens("/bookmarks", Icons.Filled.Favorite, title = "Bookmarks")
}

val bottomNavItems = listOf(
    NavScreens.Home,
    NavScreens.Bookmarks
)
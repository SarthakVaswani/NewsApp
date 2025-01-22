package com.example.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0),
                    bottomBar = { }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Transparent
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

                        AnimatedVisibility(
                            visible = isBottomBarVisible.value,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .padding(bottom = 32.dp, top = 4.dp)
                                    .height(53.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp)),
                                shape = RoundedCornerShape(24.dp),
                                tonalElevation = 8.dp,
                                shadowElevation = 8.dp,
                                color = Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFFF5252).copy(alpha = 0.95f),
                                            RoundedCornerShape(24.dp)
                                        ),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val currentRoute =
                                        navController.currentBackStackEntryAsState().value?.destination?.route
                                    bottomNavItems.forEach { item ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        color = Color.White
                                                    )
                                                ) {
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                                .padding(vertical = 8.dp)
                                        ) {
                                            val isSelected = currentRoute == item.route
                                            val color = if (isSelected) {
                                                Color.White
                                            } else {
                                                Color.White.copy(alpha = 0.7f)
                                            }

                                            Image(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .scale(if (isSelected) 1.2f else 1f),
                                                imageVector = item.icon,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(color)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                color = color
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



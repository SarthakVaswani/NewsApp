package com.example.newsapp.presentation.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsapp.presentation.home.NewsListView
import com.example.newsapp.utils.NavRoute

@Composable
fun BookmarksScreen(navController: NavController) {
    val viewModel: BookmarksViewModel = hiltViewModel()
    val bookmarks = viewModel.getBookmarks().collectAsState(initial = listOf())
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)  // Match HomeScreen padding
            .padding(top = 24.dp, bottom = 16.dp)  // Match HomeScreen padding
    ) {
        Text(
            "Bookmarks", 
            fontSize = 30.sp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.size(16.dp))
        NewsListView(bookmarks.value, onClick = {
            navController.navigate(NavRoute.createNewsDetailsRoute(it, true))
        })
    }
}
package com.example.newsapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapp.data.modal.News
import com.example.newsapp.presentation.State
import com.example.newsapp.utils.NavRoute

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    val searchText = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 16.dp)
    ) {
        SearchBar(
            text = searchText.value,
            onSearch = {
                searchText.value = it
                viewModel.getNews(searchQuery = it)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        //categories

        when (uiState.value) {
            is State.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    CircularProgressIndicator()
                    Text("Loading...")
                }
            }

            is State.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text("Failed!")
                    Text((uiState.value as State.Error).error)
                    Button(onClick = { viewModel.getNews(searchText.value) }) {
                        Text("Retry")
                    }
                }
            }

            is State.Success -> {
                val data = (uiState.value as State.Success).data
                NewsListView(data.news) {
                    navController.navigate(NavRoute.createNewsDetailsRoute(it))
                }

            }

        }
    }
}


@Composable
fun NewsListView(news: List<News>, onClick: (News) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item { 
            Text(
                "News",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            ) 
        }
        items(news) { article ->
            NewsItem(article, onClick = { onClick(article) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(text: String, onSearch: (String) -> Unit) {
    val isActive = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { isActive.value = true },
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isActive.value) {
                OutlinedTextField(
                    value = text,
                    onValueChange = onSearch,
                    placeholder = {
                        Text(
                            "Search",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    "Search",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            IconButton(
                onClick = {
                    if (!isActive.value) {
                        isActive.value = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun NewsItem(news: News, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)  // Increased padding between items
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.5f))  // Darker overlay
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = news.image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)  // Increased padding
        ) {
            Text(
                text = news.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.85f),  // Limit width for better readability
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = news.authors?.joinToString(", ") ?: "",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = news.publish_date,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
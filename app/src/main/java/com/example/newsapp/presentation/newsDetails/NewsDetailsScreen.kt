package com.example.newsapp.presentation.newsDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapp.data.modal.News
import com.example.newsapp.presentation.State


@Composable
fun NewsScreenDetails(navController: NavController, news: News) {
    NewsDetails(news = news,navController)
}

@Composable
fun NewsDetails(news: News,navController: NavController) {
    val viewModel: NewsDetailsViewModel = hiltViewModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray.copy(alpha = 0.1f))
    ) {
        AsyncImage(
            model = news.image, contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { // to tell section is scroll
            val (backBtn, topSpace, summary, newsContent) = createRefs()

            Spacer(modifier = Modifier
                .height(350.dp)
                .constrainAs(topSpace) {
                    top.linkTo(parent.top)
                })
            Image(
                imageVector = Icons.Filled.ArrowBack, contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Box(modifier = Modifier
                .constrainAs(newsContent) {
                    top.linkTo(topSpace.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.wrapContent // check
                }
                .padding(top = 24.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .padding(vertical = 50.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = news.text, fontSize = 14.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(8.dp)
                    .constrainAs(summary) {
                        top.linkTo(topSpace.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(topSpace.bottom)
                    }
            ) {
                Text(text = news.publish_date, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = news.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = news.authors?.joinToString(", ") ?: "", fontSize = 10.sp)
            }

        }
        Image(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .height(48.dp)
                .width(84.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    viewModel.addNews(news)
                }
        )
    }

    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    LaunchedEffect(state) {
        if (state.value is State.Success) {
            Toast.makeText(context, "Added to Fav", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
        }
    }
}
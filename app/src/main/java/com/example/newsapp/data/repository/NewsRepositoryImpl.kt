package com.example.newsapp.data.repository

import com.example.newsapp.data.response.NewsResponse
import com.example.newsapp.data.web.NewsAPI
import com.example.newsapp.domain.repository.NewsRepository
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(val api: NewsAPI) : NewsRepository {
    override suspend fun getNews(
        country: String,
        text: String?,
        language: String?
    ): Response<NewsResponse> {
        return api.getNews(country, text.toString(), language)
    }
}
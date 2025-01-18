package com.example.newsapp.domain.usecase

import com.example.newsapp.data.response.NewsResponse
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        country: String,
        text: String?,
        language: String?,
    ): NewsResponse {
        val response = repository.getNews(country, text, language)
        if (response.body() == null) {
            if (response.code() == 404)
                throw Exception("No News Found")
            else
                throw Exception("Failed to get news")

        }
        return repository.getNews(country, text, language).body()!!
    }
}
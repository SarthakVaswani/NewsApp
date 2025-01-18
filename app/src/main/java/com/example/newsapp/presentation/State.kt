package com.example.newsapp.presentation

import com.example.newsapp.data.modal.News

sealed class State<out T> {
    data object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val error: String) : State<Nothing>()

}
package com.example.newsapp.presentation.newsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.database.NewsDB
import com.example.newsapp.data.modal.News
import com.example.newsapp.presentation.BookmarkState
import com.example.newsapp.presentation.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    database: NewsDB
) : ViewModel() {

    private val _state = MutableStateFlow<State<BookmarkState>>(State.Loading)
    val state = _state as StateFlow<State<BookmarkState>>

    private val newsDao = database.newsDAO()

    fun addNews(news: News) {
        try {
            _state.value = State.Loading
            viewModelScope.launch {
                newsDao.addNews(news)
                _state.tryEmit(State.Success(BookmarkState.Added))
            }
        } catch (e: Exception) {
            _state.value = State.Error(e.message.toString())
        }
    }

    fun deleteNews(news: News) {
        try {
            _state.value = State.Loading
            viewModelScope.launch {
                newsDao.deleteNews(news)
                _state.tryEmit(State.Success(BookmarkState.Removed))
            }
        } catch (e: Exception) {
            _state.value = State.Error(e.message.toString())
        }
    }
}


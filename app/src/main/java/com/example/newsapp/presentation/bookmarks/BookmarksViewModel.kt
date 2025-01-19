package com.example.newsapp.presentation.bookmarks

import androidx.lifecycle.ViewModel
import com.example.newsapp.data.database.NewsDB
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(database: NewsDB) : ViewModel() {

    private val newsDao = database.newsDAO()

    fun getBookmarks() = newsDao.getNews()
}
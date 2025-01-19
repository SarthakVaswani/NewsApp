package com.example.newsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.data.dao.NewsDao
import com.example.newsapp.data.modal.News

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class NewsDB : RoomDatabase() {
    abstract fun newsDAO(): NewsDao

    companion object {
        private const val DB_NAME = "news_db"

        @Volatile
        private var INSTANCE: NewsDB? = null

        @JvmStatic
        fun getDatabase(context: Context): NewsDB {
            return INSTANCE ?: synchronized(true) {
                Room.databaseBuilder(context, NewsDB::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }

        }
    }

}
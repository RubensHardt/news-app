package com.rubenshardt.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rubenshardt.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}
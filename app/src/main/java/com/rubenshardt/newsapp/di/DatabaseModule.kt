package com.rubenshardt.newsapp.di

import android.content.Context
import androidx.room.Room
import com.rubenshardt.newsapp.data.database.ArticlesDao
import com.rubenshardt.newsapp.data.database.ArticlesDatabase
import com.rubenshardt.newsapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): ArticlesDatabase = Room.databaseBuilder(
        context,
        ArticlesDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesDao(database: ArticlesDatabase): ArticlesDao = database.articlesDao()
}
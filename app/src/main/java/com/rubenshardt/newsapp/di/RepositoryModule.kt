package com.rubenshardt.newsapp.di

import com.rubenshardt.newsapp.data.database.ArticlesDao
import com.rubenshardt.newsapp.data.datasources.LocalDataSource
import com.rubenshardt.newsapp.data.datasources.LocalDataSourceImpl
import com.rubenshardt.newsapp.data.datasources.RemoteDataSource
import com.rubenshardt.newsapp.data.datasources.RemoteDataSourceImpl
import com.rubenshardt.newsapp.data.network.ArticlesApi
import com.rubenshardt.newsapp.data.repositories.ArticlesRepository
import com.rubenshardt.newsapp.data.repositories.ArticlesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesLocalDataSource(articlesDao: ArticlesDao): LocalDataSource {
        return LocalDataSourceImpl(articlesDao)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(articlesApi: ArticlesApi): RemoteDataSource {
        return RemoteDataSourceImpl(articlesApi)
    }

    @Singleton
    @Provides
    fun providesArticlesRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): ArticlesRepository {
        return ArticlesRepositoryImpl(remoteDataSource, localDataSource)
    }
}
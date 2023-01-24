package com.rubenshardt.newsapp.data.datasources

import com.rubenshardt.newsapp.data.network.ArticlesApi
import com.rubenshardt.newsapp.data.network.ArticlesResponse
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface RemoteDataSource {
    fun getArticles(category: String): Single<Response<ArticlesResponse>>
}

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val articleApi: ArticlesApi
): RemoteDataSource {
    override fun getArticles(category: String): Single<Response<ArticlesResponse>> {
        return articleApi.getArticles(category)
    }
}
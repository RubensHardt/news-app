package com.rubenshardt.newsapp.data.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {

    @GET("news")
    fun getArticles(@Query("category") category: String): Single<Response<ArticlesResponse>>
}
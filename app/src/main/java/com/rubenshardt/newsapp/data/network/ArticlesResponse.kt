package com.rubenshardt.newsapp.data.network

import com.google.gson.annotations.SerializedName
import com.rubenshardt.newsapp.models.Article

data class ArticlesResponse(
    val category: String,
    @SerializedName("data")
    val articles: List<Article>,
    val success: Boolean
)
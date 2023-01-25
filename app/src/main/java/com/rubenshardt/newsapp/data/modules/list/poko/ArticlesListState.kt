package com.rubenshardt.newsapp.data.modules.list.poko

import com.rubenshardt.newsapp.models.Article

class ArticlesListState(
    val category: String,
    val isLoading: Boolean,
    val articles: List<Article>? = null,
    val error: ArticlesError? = null
) {
    val isEmpty: Boolean = articles.isNullOrEmpty()
}
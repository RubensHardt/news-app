package com.rubenshardt.newsapp.data.datasources

import com.rubenshardt.newsapp.data.database.ArticlesDao
import com.rubenshardt.newsapp.models.Article
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

interface LocalDataSource {
    fun readArticles(category: String): Flowable<List<Article>>
    fun insertArticles(articles: List<Article>)
    fun updateArticle(article: Article)
}

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val articlesDao: ArticlesDao
): LocalDataSource {

    override fun readArticles(category: String): Flowable<List<Article>> {
        return articlesDao.readArticles(category)
    }

    override fun insertArticles(articles: List<Article>) {
        articlesDao.insertArticles(articles)
    }

    override fun updateArticle(article: Article) {
        return articlesDao.updateArticle(article)
    }
}
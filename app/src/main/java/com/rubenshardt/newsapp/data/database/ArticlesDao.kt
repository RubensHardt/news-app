package com.rubenshardt.newsapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rubenshardt.newsapp.models.Article
import com.rubenshardt.newsapp.utils.Constants.ARTICLES_TABLE
import io.reactivex.Flowable

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM $ARTICLES_TABLE WHERE category LIKE :category ORDER BY date DESC")
    fun readArticles(category: String): Flowable<List<Article>>

    @Update
    fun updateArticle(article: Article)
}
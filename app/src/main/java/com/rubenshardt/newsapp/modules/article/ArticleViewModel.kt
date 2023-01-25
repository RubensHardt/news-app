package com.rubenshardt.newsapp.modules.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rubenshardt.newsapp.data.repositories.ArticlesRepository
import com.rubenshardt.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository,
) : ViewModel() {

    private lateinit var currentArticle: Article
    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    fun setup(article: Article) {
        currentArticle = article
        _article.postValue(article)
    }

    fun markAsReadClick() {
        currentArticle.read = !currentArticle.read
        articlesRepository.updateArticle(currentArticle)
        _article.postValue(currentArticle)
    }
}
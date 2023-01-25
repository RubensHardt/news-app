package com.rubenshardt.newsapp.data.modules.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rubenshardt.newsapp.data.modules.list.poko.ArticlesError
import com.rubenshardt.newsapp.data.modules.list.poko.ArticlesListState
import com.rubenshardt.newsapp.data.repositories.ArticlesRepository
import com.rubenshardt.newsapp.data.repositories.NetworkResult
import com.rubenshardt.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository,
) : ViewModel() {

    private lateinit var category: String
    private lateinit var disposable: Disposable
    private val _articlesListState = MutableLiveData<ArticlesListState>()
    val articlesListState: LiveData<ArticlesListState> = _articlesListState

    fun selectCategory(category: String) {
        this.category = category
        getArticles()
    }

    fun refresh() {
        getArticles()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    private fun getArticles() {
        disposable = articlesRepository.getArticles(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleResponse, ::handleError)
    }

    private fun handleResponse(response: NetworkResult<List<Article>>) {
        _articlesListState.postValue(
            ArticlesListState(
                category = category,
                isLoading = response is NetworkResult.Loading,
                articles = response.data,
                error = if (response is NetworkResult.Error && !response.wasHandled) {
                    response.wasHandled = true
                    ArticlesError(true, response.message)
                } else null
            )
        )
    }

    private fun handleError(error: Throwable) {
        _articlesListState.value?.let {
            _articlesListState.postValue(
                ArticlesListState(
                    category = category,
                    isLoading = false,
                    articles = it.articles,
                    error = ArticlesError(true, error.localizedMessage)
                )
            )
        }
    }
}
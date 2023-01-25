package com.rubenshardt.newsapp.data.repositories

import com.rubenshardt.newsapp.data.datasources.LocalDataSource
import com.rubenshardt.newsapp.data.datasources.RemoteDataSource
import com.rubenshardt.newsapp.models.Article
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface ArticlesRepository {
    fun getArticles(category: String): Flowable<NetworkResult<List<Article>>>
    fun updateArticle(article: Article)
}

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): ArticlesRepository {
    private var disposable: Disposable? = null
    private var localDisposable: Disposable? = null

    override fun getArticles(category: String) = Flowable.create({ emitter ->
        var isLoading = true
        if (localDisposable?.isDisposed == false) {
            localDisposable?.dispose()
        }
        localDisposable = localDataSource.readArticles(category)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { storedArticles ->
                    if (isLoading) {
                        emitter.onNext(NetworkResult.Loading(storedArticles))
                        getRemoteArticles(category, storedArticles, emitter) {
                            isLoading = false
                        }
                    } else {
                        emitter.onNext(NetworkResult.Success(storedArticles))
                    }
                },
                {
                    emitter.onNext(
                        NetworkResult.Error(message = "Failed to get local articles")
                    )
                    localDisposable?.dispose()
                }
            )
    },
        BackpressureStrategy.BUFFER
    ).subscribeOn(Schedulers.io())

    override fun updateArticle(article: Article) {
        Completable
            .create {
                localDataSource.updateArticle(article)
            }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun getRemoteArticles(
        category: String,
        localArticles: List<Article>,
        emitter: FlowableEmitter<NetworkResult<List<Article>>>,
        callback: () -> Unit
    ) {
        disposable = remoteDataSource.getArticles(category)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response ->
                    callback()
                    val articles = response.body()?.articles ?: return@subscribe
                    articles.forEach { it.category = category }
                    localDataSource.insertArticles(articles)
                    disposable?.dispose()
                },
                {
                    callback()
                    emitter.onNext(
                        NetworkResult.Error(
                            message = "Failed to get articles",
                            data = localArticles,
                        )
                    )
                    disposable?.dispose()
                }
            )
    }
}
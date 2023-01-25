package com.rubenshardt.newsapp.data.repositories

import com.rubenshardt.newsapp.data.datasources.LocalDataSource
import com.rubenshardt.newsapp.data.datasources.RemoteDataSource
import com.rubenshardt.newsapp.data.network.ArticlesResponse
import com.rubenshardt.newsapp.models.Article
import io.mockk.every
import io.mockk.spyk
import io.reactivex.Flowable
import io.reactivex.Single
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.ClassRule
import org.junit.Test
import retrofit2.Response

class ArticlesRepositoryTest {

    private val mockedLocalDataSource = spyk<LocalDataSource>()
    private val mockedRemoteDataSource = spyk<RemoteDataSource>()
    private val articlesRepository = ArticlesRepositoryImpl(
        mockedRemoteDataSource,
        mockedLocalDataSource,
    )

    private val mockedCategory = "mockedCategory"
    private val mockedLocalArticles = listOf(
        Article(
            id = "local",
            author = "local",
            content = "local",
            date = "local",
            imageUrl = "local",
            readMoreUrl = "local",
            time = "local",
            title = "local",
            url = "local",
            category = "local",
            read =  false
        )
    )
    private val mockedRemoteArticles = Response.success(
        ArticlesResponse(
            category = mockedCategory,
            articles = listOf(
                Article(
                    id = "remote",
                    author = "remote",
                    content = "remote",
                    date = "remote",
                    imageUrl = "remote",
                    readMoreUrl = "remote",
                    time = "remote",
                    title = "remote",
                    url = "remote",
                    category = "remote",
                    read =  false
                )
            ),
            success = true
        )
    )

    @Test
    fun `Assert repository returns local articles first when remote repository works as expected`() {
        //given
        every { mockedLocalDataSource.readArticles(mockedCategory) } returns Flowable.just(mockedLocalArticles)

        //when
        val disposable = articlesRepository.getArticles(mockedCategory).test()

        //then
        val result = disposable.values()[0]
        assert(result is NetworkResult.Loading)
        TestCase.assertEquals(result.data, mockedLocalArticles)
    }

    @Test
    fun `Assert repository returns remote articles when remote repository works as expected`() {
        //given
        every { mockedLocalDataSource.readArticles(mockedCategory) } returns Flowable.just(mockedLocalArticles, mockedRemoteArticles.body()?.articles)
        every { mockedRemoteDataSource.getArticles(mockedCategory) } returns Single.just(mockedRemoteArticles)

        //when
        val disposable = articlesRepository.getArticles(mockedCategory).test()

        //then
        val localResult = disposable.values()[0]
        assert(localResult is NetworkResult.Loading)
        TestCase.assertEquals(localResult.data, mockedLocalArticles)
        val result = disposable.values()[1]
        assert(result is NetworkResult.Success)
        assertEquals(result.data, mockedRemoteArticles.body()?.articles)
    }

    @Test
    fun `Assert repository returns local articles when remote repository fails`() {
        //given
        every { mockedLocalDataSource.readArticles(mockedCategory) } returns Flowable.just(mockedLocalArticles)
        every { mockedRemoteDataSource.getArticles(mockedCategory) } returns Single.error(Throwable())

        //when
        val disposable = articlesRepository.getArticles(mockedCategory).test()

        //then
        val result = disposable.values().last()
        TestCase.assertEquals(result.data, mockedLocalArticles)
    }

    companion object
    {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }
}
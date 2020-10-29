package com.dhimandasgupta.myjetpacknews.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhimandasgupta.data.domain.ArticleDomainModel
import com.dhimandasgupta.data.domain.NewsDomainModel
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.Params
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.myjetpacknews.rule.MainCoroutineRule
import com.dhimandasgupta.myjetpacknews.rule.runBlockingTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SingleSourceViewModelSpec {
    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val fakeNewsUseCase: NewsUseCase = FakeNewsUseCase()

    private lateinit var singleSourceViewModel: SingleSourceViewModel

    @Before
    fun init() {
        Dispatchers.setMain(mainCoroutineRule.testDispatcher)
        singleSourceViewModel = SingleSourceViewModel(fakeNewsUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        mainCoroutineRule.testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `live data should have value with SuccessUI Model when the call is successful`() {
        mainCoroutineRule.runBlockingTest {
            fakeNewsUseCase.getEverythingByQuery(Params(query = "google"))
            assertNotNull(singleSourceViewModel.newsUiState.value)

            // Testing Current Source from the Ui state
            assertNotNull(singleSourceViewModel.newsUiState.value?.currentSource)
            assertEquals("google", singleSourceViewModel.newsUiState.value?.currentSource?.title)
            assertTrue(singleSourceViewModel.newsUiState.value?.currentSource?.selected!!)

            // Testing Ui Models from Ui state
            assertNotNull(singleSourceViewModel.newsUiState.value?.uiModels)
            assertTrue(singleSourceViewModel.newsUiState.value?.uiModels is SuccessUIModel)

            val successUIModel = singleSourceViewModel.newsUiState.value?.uiModels as SuccessUIModel
            assertNotNull(successUIModel)
            assertNotNull(successUIModel.articlesUIModel)
            assertTrue(successUIModel.articlesUIModel.articles.isNotEmpty())
            assertEquals(1, successUIModel.articlesUIModel.articles.size)
            assertEquals("some source", successUIModel.articlesUIModel.articles[0].sourceName)
            assertEquals("some title", successUIModel.articlesUIModel.articles[0].title)
            assertEquals("some author", successUIModel.articlesUIModel.articles[0].author)
            assertEquals("some content", successUIModel.articlesUIModel.articles[0].content)
            assertEquals("some description", successUIModel.articlesUIModel.articles[0].description)
            assertEquals("some date", successUIModel.articlesUIModel.articles[0].publishedAt)
            assertEquals("https://someimage.jpeg", successUIModel.articlesUIModel.articles[0].imageUrl)
            assertEquals("https://someurl", successUIModel.articlesUIModel.articles[0].url)

            // Testing All Sources from the Ui stateMainCoroutineRule
            assertNotNull(singleSourceViewModel.newsUiState.value?.allSources)
            assertEquals(13, singleSourceViewModel.newsUiState.value?.allSources?.size)
        }
    }
}

private class FakeNewsUseCase : NewsUseCase {
    override suspend fun getEverythingByQuery(params: Params) = NewsDomainModel(
        articles = listOf(
            ArticleDomainModel(
                sourceName = "some source",
                author = "some author",
                title = "some title",
                description = "some description",
                url = "https://someurl",
                imageUrl = "https://someimage.jpeg",
                publishedAt = "some date",
                content = "some content"
            )
        )
    )
}

package com.dhimandasgupta.data.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.Exception

@RunWith(JUnit4::class)
class NewsUseCaseSpec {
    @MockK
    lateinit var newsRepository: NewsRepository

    lateinit var newsUseCase: NewsUseCase

    @Before
    fun init() {
        MockKAnnotations.init(this)
        newsUseCase = NewsUseCase(newsRepository)
    }

    @Test
    fun `should return domain model with exception when api fails`() = runBlocking {
        val returnedNewsDomainModel = NewsDomainModel(
            exception = ExceptionDomainModel(
                exception = Exception("some random exception"),
                source = SourceDomainModel(sourceName = "some_query")
            )
        )
        coEvery { newsUseCase.getEverythingByQuery(Params("some_query")) } returns returnedNewsDomainModel

        val newsDomainModel = newsUseCase.getEverythingByQuery(Params("some_query"))
        assertNotNull(newsDomainModel)
        assertNotNull(newsDomainModel.articles)
        assertEquals(0, newsDomainModel.articles.size)
        assertNotNull(newsDomainModel.exception)
        assertTrue(newsDomainModel.exception?.source?.selected ?: false)
        assertEquals("some_query", newsDomainModel.exception?.source?.sourceName)
    }

    @Test
    fun `should return domain model with list of news articles when api is successful`() = runBlocking {
        val returnedNewsDomainModel = NewsDomainModel(
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
        coEvery { newsUseCase.getEverythingByQuery(Params("some_query")) } returns returnedNewsDomainModel

        val newsDomainModel = newsUseCase.getEverythingByQuery(Params("some_query"))
        assertNotNull(newsDomainModel)
        assertNotNull(newsDomainModel.articles)
        assertEquals(1, newsDomainModel.articles.size)
        assertEquals("some author", newsDomainModel.articles[0].author)
        assertEquals("some content", newsDomainModel.articles[0].content)
        assertEquals("some description", newsDomainModel.articles[0].description)
        assertEquals("some date", newsDomainModel.articles[0].publishedAt)
        assertEquals("some source", newsDomainModel.articles[0].sourceName)
        assertEquals("some title", newsDomainModel.articles[0].title)
        assertEquals("https://someurl", newsDomainModel.articles[0].url)
        assertEquals("https://someimage.jpeg", newsDomainModel.articles[0].imageUrl)
        Assert.assertNull(newsDomainModel.exception)
    }
}

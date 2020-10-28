package com.dhimandasgupta.data.presentaion

import com.dhimandasgupta.data.domain.ArticleDomainModel
import com.dhimandasgupta.data.domain.ExceptionDomainModel
import com.dhimandasgupta.data.domain.NewsDomainModel
import com.dhimandasgupta.data.domain.SourceDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UIConverterSpec {
    @Test
    fun `should return error ui model when domain model has exception`() {
        val domainModel = NewsDomainModel(
            exception = ExceptionDomainModel(
                exception = Exception("some exception"),
                source = SourceDomainModel(
                    sourceName = "some source",
                    selected = true
                )
            )
        )

        val uiModel = domainModel.toUIModel()

        assertTrue(uiModel is ErrorUIModel)
        assertFalse(uiModel is SuccessUIModel)
        assertFalse(uiModel is LoadingUIModel)

        val errorUIModel = uiModel as ErrorUIModel
        assertNotNull(errorUIModel.exception)
        assertEquals("some exception", errorUIModel.exception.message)
        assertNotNull(errorUIModel.source)
        assertEquals("some source", errorUIModel.source.title)
        assertTrue(errorUIModel.source.selected)
    }

    @Test
    fun `should return empty success ui model when domain model has no exception`() {
        val domainModel = NewsDomainModel()

        val uiModel = domainModel.toUIModel()

        assertFalse(uiModel is ErrorUIModel)
        assertTrue(uiModel is SuccessUIModel)
        assertFalse(uiModel is LoadingUIModel)

        val successUIModel = uiModel as SuccessUIModel
        assertNotNull(successUIModel)
        assertNotNull(successUIModel.articlesUIModel)
        assertNotNull(successUIModel.articlesUIModel.articles)
        assertTrue(successUIModel.articlesUIModel.articles.isEmpty())
    }

    @Test
    fun `should return success ui model when domain model has valid articles`() {
        val domainModel = NewsDomainModel(
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

        val uiModel = domainModel.toUIModel()

        assertFalse(uiModel is ErrorUIModel)
        assertTrue(uiModel is SuccessUIModel)
        assertFalse(uiModel is LoadingUIModel)

        val successUIModel = uiModel as SuccessUIModel
        assertNotNull(successUIModel)
        assertNotNull(successUIModel.articlesUIModel)
        assertNotNull(successUIModel.articlesUIModel.articles)
        assertEquals(1, successUIModel.articlesUIModel.articles.size)
        assertEquals("some author", successUIModel.articlesUIModel.articles[0].author)
        assertEquals("some content", successUIModel.articlesUIModel.articles[0].content)
        assertEquals("some description", successUIModel.articlesUIModel.articles[0].description)
        assertEquals("some date", successUIModel.articlesUIModel.articles[0].publishedAt)
        assertEquals("some source", successUIModel.articlesUIModel.articles[0].sourceName)
        assertEquals("some title", successUIModel.articlesUIModel.articles[0].title)
        assertEquals("https://someurl", successUIModel.articlesUIModel.articles[0].url)
        assertEquals("https://someimage.jpeg", successUIModel.articlesUIModel.articles[0].imageUrl)
    }
}

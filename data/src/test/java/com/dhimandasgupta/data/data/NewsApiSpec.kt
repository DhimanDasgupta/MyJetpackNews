package com.dhimandasgupta.data.data

import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.data.api.NewsRequestHeaderInterceptor
import com.dhimandasgupta.data.domain.SourceDomainModel
import com.dhimandasgupta.data.utils.TestResourceReader
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class NewsApiSpec {
    private val server: MockWebServer = MockWebServer()

    private lateinit var newsApi: NewsApi
    private lateinit var newsService: NewsService

    @Before
    fun init() {
        server.start(8000)

        val headerInterceptor = NewsRequestHeaderInterceptor("some_api_key")
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        newsApi = Retrofit.Builder()
            .client(okhttp)
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(NewsApi::class.java)

        newsService = NewsServiceImpl(newsApi)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun `should return domain exception when api returns empty body`() = runBlocking {
        server.apply {
            enqueue(MockResponse().setResponseCode(200).setBody(TestResourceReader.readTestResourceFile("empty_response.json")))
        }

        val domainModel = newsService.getEverythingByQuery("some_source")

        assertNotNull(domainModel.articles)
        assertNotNull(domainModel.exception)
        assertEquals(SourceDomainModel("some_source", true), domainModel.exception?.source)
    }

    @Test
    fun `should return domain model when api returns valid response`() = runBlocking {
        server.apply {
            enqueue(MockResponse().setResponseCode(200).setBody(TestResourceReader.readTestResourceFile("success_response.json")))
        }

        val domainModel = newsService.getEverythingByQuery("some_source")

        assertNull(domainModel.exception)
        assertNotNull(domainModel.articles)
        assertEquals(1, domainModel.articles.size)
        assertEquals("some author", domainModel.articles[0].author)
        assertEquals("some content", domainModel.articles[0].content)
        assertEquals("some description.", domainModel.articles[0].description)
        assertEquals("2020-10-28T17:04:08Z", domainModel.articles[0].publishedAt)
        assertEquals("some name", domainModel.articles[0].sourceName)
        assertEquals("some title", domainModel.articles[0].title)
        assertEquals("https://someurl", domainModel.articles[0].url)
        assertEquals("https://some_image.jpeg", domainModel.articles[0].imageUrl)
    }

    @Test
    fun `should return domain exception when api returns malformed json`() = runBlocking {
        server.apply {
            enqueue(MockResponse().setResponseCode(200).setBody(TestResourceReader.readTestResourceFile("malformed_response.json")))
        }

        val domainModel = newsService.getEverythingByQuery("some_source")

        assertNotNull(domainModel.articles)
        assertNotNull(domainModel.exception)
        assertEquals(SourceDomainModel("some_source", true), domainModel.exception?.source)
    }
}

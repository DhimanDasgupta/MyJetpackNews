package com.dhimandasgupta.data.data

import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.domain.NewsDomainModel
import com.dhimandasgupta.data.domain.NewsRepository
import com.dhimandasgupta.data.domain.errorDomainModel
import com.dhimandasgupta.data.domain.toDomainModel
import kotlinx.coroutines.delay
import java.lang.Exception

interface NewsService {
    suspend fun getEverythingByQuery(query: String): NewsDomainModel
}

class NewsServiceImpl(private val newsApi: NewsApi) : NewsService {
    override suspend fun getEverythingByQuery(query: String) = try {
        delay(2000)
        newsApi.getEverythingByQuery(query).toDomainModel()
    } catch (e: Exception) {
        errorDomainModel(query, e)
    }
}

class NewsRepositoryImpl(private val newsService: NewsService) : NewsRepository {
    override suspend fun getEverythingByQuery(query: String) = newsService.getEverythingByQuery(query)
}
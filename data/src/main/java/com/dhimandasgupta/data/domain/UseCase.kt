package com.dhimandasgupta.data.domain

class NewsUseCase(private val repository: NewsRepository) : BaseNewsUseCase {
    override suspend fun getEverythingByQuery(params: Params): NewsDomainModel {
        return repository.getEverythingByQuery(params.query)
    }
}

interface BaseNewsUseCase {
    suspend fun getEverythingByQuery(params: Params): NewsDomainModel
}

class Params(val query: String)

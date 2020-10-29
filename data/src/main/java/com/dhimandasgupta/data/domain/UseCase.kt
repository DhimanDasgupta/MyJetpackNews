package com.dhimandasgupta.data.domain

class NewsUseCaseImpl(private val repository: NewsRepository) : NewsUseCase {
    override suspend fun getEverythingByQuery(params: Params): NewsDomainModel {
        return repository.getEverythingByQuery(params.query)
    }
}

interface NewsUseCase {
    suspend fun getEverythingByQuery(params: Params): NewsDomainModel
}

class Params(val query: String)

package com.dhimandasgupta.data.domain

interface NewsRepository {
    suspend fun getEverythingByQuery(query: String): NewsDomainModel
}

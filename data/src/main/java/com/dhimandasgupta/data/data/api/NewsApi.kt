package com.dhimandasgupta.data.data.api

import com.dhimandasgupta.data.data.models.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "place your api key here"

interface NewsApi {
    @GET("everything")
    suspend fun getEverythingByQuery(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NetworkResponse
}
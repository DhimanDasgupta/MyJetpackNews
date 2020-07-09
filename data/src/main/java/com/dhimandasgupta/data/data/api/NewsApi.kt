package com.dhimandasgupta.data.data.api

import com.dhimandasgupta.data.data.models.NetworkResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_NEWS_API_DOMAIN = "https://newsapi.org/v2/"
private const val API_KEY = "place your api key here"

interface NewsApi {
    @GET("everything")
    suspend fun getEverythingByQuery(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NetworkResponse
}

class NewsApiGenerator {
    companion object {
        fun getNewsApi(): NewsApi {
            val retrofit = Retrofit.Builder().
            baseUrl(BASE_NEWS_API_DOMAIN).
            addConverterFactory(
                GsonConverterFactory.create()
            ).build()

            return retrofit.create(NewsApi::class.java)
        }
    }
}
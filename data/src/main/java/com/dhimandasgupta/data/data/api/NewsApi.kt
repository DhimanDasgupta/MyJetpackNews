package com.dhimandasgupta.data.data.api

import com.dhimandasgupta.data.data.models.NetworkResponse
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    suspend fun getEverythingByQuery(
        @Query("q") query: String
    ): NetworkResponse
}

class NewsRequestHeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestWithHeaders = request.newBuilder()
            .addHeader("X-Api-Key", apiKey)
            .build()

        return chain.proceed(requestWithHeaders)
    }
}

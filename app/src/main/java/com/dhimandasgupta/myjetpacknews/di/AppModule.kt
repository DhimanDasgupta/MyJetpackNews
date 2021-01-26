package com.dhimandasgupta.myjetpacknews.di

import com.dhimandasgupta.data.data.NewsRepositoryImpl
import com.dhimandasgupta.data.data.NewsService
import com.dhimandasgupta.data.data.NewsServiceImpl
import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.data.api.NewsRequestHeaderInterceptor
import com.dhimandasgupta.data.domain.NewsRepository
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.NewsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

private const val BASE_NEWS_API_DOMAIN = "https://newsapi.org/v2/"
private const val API_KEY = "8aed68a4448b4c50b1c72a0fb83be86a"

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @BaseUrl
    @Provides
    @Singleton
    fun provideBaseUrl(): String = BASE_NEWS_API_DOMAIN

    @ApiKey
    @Provides
    @Singleton
    fun provideApiKey(): String = API_KEY

    @Provides
    @Singleton
    fun provideHeaderInterceptor(@ApiKey apiKey: String): Interceptor = NewsRequestHeaderInterceptor(apiKey = apiKey)

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrl baseUrl: String, okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    // ToDO: Better scoping for the service, repo and usecase
    @Provides
    @Singleton
    fun provideNewsService(newsApi: NewsApi): NewsService = NewsServiceImpl(newsApi)

    @Provides
    @Singleton
    fun provideNewsRepository(newsService: NewsService): NewsRepository = NewsRepositoryImpl(newsService)

    @Provides
    @Singleton
    fun provideNewsUseCase(newsRepository: NewsRepository): NewsUseCase = NewsUseCaseImpl(newsRepository)
}

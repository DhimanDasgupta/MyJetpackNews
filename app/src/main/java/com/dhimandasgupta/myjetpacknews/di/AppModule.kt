package com.dhimandasgupta.myjetpacknews.di

import com.dhimandasgupta.data.data.NewsRepositoryImpl
import com.dhimandasgupta.data.data.NewsServiceImpl
import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.data.api.NewsRequestHeaderInterceptor
import com.dhimandasgupta.data.domain.NewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

private const val BASE_NEWS_API_DOMAIN = "https://newsapi.org/v2/"
private const val API_KEY = "Enter your api key"

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = BASE_NEWS_API_DOMAIN

    @ApiKey
    @Provides
    fun provideApiKey(): String = API_KEY

    @Provides
    fun provideHeaderInterceptor(@ApiKey apiKey: String): Interceptor = NewsRequestHeaderInterceptor(apiKey = apiKey)

    @Provides
    fun provideOkHttpClient(headerInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    @Provides
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    fun provideRetrofit(@BaseUrl baseUrl: String, okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityModule {
    @Provides
    fun provideNewsService(newsApi: NewsApi): NewsServiceImpl = NewsServiceImpl(newsApi)

    @Provides
    fun provideNewsRepository(newsService: NewsServiceImpl): NewsRepositoryImpl = NewsRepositoryImpl(newsService)

    @Provides
    fun provideNewsUseCase(newsRepository: NewsRepositoryImpl): NewsUseCase = NewsUseCase(newsRepository)
}
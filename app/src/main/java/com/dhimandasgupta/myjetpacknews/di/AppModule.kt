package com.dhimandasgupta.myjetpacknews.di

import com.dhimandasgupta.data.data.NewsRepositoryImpl
import com.dhimandasgupta.data.data.NewsServiceImpl
import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.domain.NewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

private const val BASE_NEWS_API_DOMAIN = "https://newsapi.org/v2/"

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @BaseUrl
    @Provides
    fun provideBaseUrl() = BASE_NEWS_API_DOMAIN

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

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
    fun provideNewsUseCase(newsApi: NewsApi): NewsUseCase = NewsUseCase(provideNewsRepository(newsApi))

    @Provides
    fun provideNewsRepository(newsApi: NewsApi): NewsRepositoryImpl = NewsRepositoryImpl(provideNewsService(newsApi))

    @Provides
    fun provideNewsService(newsApi: NewsApi): NewsServiceImpl = NewsServiceImpl(newsApi)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl
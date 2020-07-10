package com.dhimandasgupta.myjetpacknews.di

import com.dhimandasgupta.data.data.NewsRepositoryImpl
import com.dhimandasgupta.data.data.NewsServiceImpl
import com.dhimandasgupta.data.data.api.NewsApi
import com.dhimandasgupta.data.data.api.NewsApiGenerator
import com.dhimandasgupta.data.domain.NewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    fun provideNewsUseCase(): NewsUseCase = NewsUseCase(provideNewsRepository())

    @Provides
    fun provideNewsRepository(): NewsRepositoryImpl = NewsRepositoryImpl(provideNewsService())

    @Provides
    fun provideNewsService(): NewsServiceImpl = NewsServiceImpl(provideNewsApi())

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi = NewsApiGenerator.getNewsApi()
}
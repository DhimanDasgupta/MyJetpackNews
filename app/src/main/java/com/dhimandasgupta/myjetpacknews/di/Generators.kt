package com.dhimandasgupta.myjetpacknews.di

import com.dhimandasgupta.data.data.NewsRepositoryImpl
import com.dhimandasgupta.data.data.NewsServiceImpl
import com.dhimandasgupta.data.data.api.NewsApiGenerator
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.myjetpacknews.viewmodel.NewsViewModelFactory

object Generators {
    fun provideNewsViewModelFactory() = NewsViewModelFactory(provideNewsUseCase())

    private fun provideNewsUseCase() = NewsUseCase(provideNewsRepository())

    private fun provideNewsRepository() = NewsRepositoryImpl(provideNewsService())

    private fun provideNewsService() = NewsServiceImpl(provideNewsApi())

    private fun provideNewsApi() = NewsApiGenerator.getNewsApi()
}
package com.dhimandasgupta.myjetpacknews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.Params
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.NewsUiState
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.initialNewsUiState
import com.dhimandasgupta.data.presentaion.mapSource
import com.dhimandasgupta.data.presentaion.toUIModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class NewsViewModel(
    private val useCase: NewsUseCase
) : ViewModel() {
    private val newsUiStateMutableLiveData = MutableLiveData<NewsUiState>().also {
        it.value = initialNewsUiState
    }
    val newsUiState: LiveData<NewsUiState> = newsUiStateMutableLiveData

    init {
        val source = newsUiStateMutableLiveData.value?.allSources?.first()
        source?.let { noNullSource ->
            fetchNewsFromSource(noNullSource)
        }
    }

    private var currentJob: Job? = null


    fun fetchNewsFromSource(source: Source) {
        val newSources = mapSource(query = source.title)
        val currentSource = newSources.first { s: Source -> s.selected }

        newsUiStateMutableLiveData.postValue(
            newsUiStateMutableLiveData.value?.copy(
                currentSource = currentSource,
                allSources = newSources
            )
        )

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            kotlin.runCatching {
                newsUiStateMutableLiveData.postValue(
                    newsUiStateMutableLiveData.value?.copy(
                        uiModels = LoadingUIModel(source = currentSource),
                        currentSource = currentSource,
                        allSources = newSources
                    )
                )
                return@runCatching useCase.getEverythingByQuery(Params(query = source.title)).toUIModel()
            }.onSuccess { uiModels ->
                newsUiStateMutableLiveData.postValue(
                    newsUiStateMutableLiveData.value?.copy(
                        uiModels = uiModels,
                        currentSource = currentSource,
                        allSources = newSources
                    )
                )
            }.onFailure { throwable ->
                newsUiStateMutableLiveData.postValue(
                    newsUiStateMutableLiveData.value?.copy(
                        uiModels = ErrorUIModel(Exception(throwable.localizedMessage), source = currentSource),
                        currentSource = currentSource,
                        allSources = newSources)
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(
    private val useCase: NewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(useCase) as T
        }
        throw IllegalAccessException("Please make sure that, you are passing tht correct parameters to create the ViewModel")
    }

}
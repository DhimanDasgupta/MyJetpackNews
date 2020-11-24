package com.dhimandasgupta.myjetpacknews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.Params
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.LoadingUIModel
import com.dhimandasgupta.data.presentaion.NewsUiState
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.data.presentaion.UIModels
import com.dhimandasgupta.data.presentaion.initialNewsUiState
import com.dhimandasgupta.data.presentaion.mapSource
import com.dhimandasgupta.data.presentaion.sources
import com.dhimandasgupta.data.presentaion.toUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivityViewModel @ViewModelInject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {
    // Variables used for Single Source Composable
    private val newsUiStateMutableLiveData = MutableLiveData<NewsUiState>().also {
        it.value = initialNewsUiState
    }
    val newsUiState: LiveData<NewsUiState> = newsUiStateMutableLiveData
    private var currentJob: Job? = null

    // Variable used for Multi Source Composable
    val sourcesLiveData: LiveData<List<Source>> = MutableLiveData(sources)
    private val sourceWithUiMap = HashMap<Source, UIModels>()

    init {
        val source = newsUiStateMutableLiveData.value?.allSources?.first()
        source?.let { noNullSource ->
            fetchNewsFromSource(noNullSource)
        }
    }

    fun fetchNewsFromSource(source: Source) {
        val newSources = mapSource(query = source.title)
        val currentSource = newSources.first { s: Source -> s.selected }

        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                newsUiStateMutableLiveData.postValue(
                    newsUiStateMutableLiveData.value?.copy(
                        uiModels = LoadingUIModel(source = currentSource),
                        currentSource = currentSource,
                        allSources = newSources
                    )
                )
                return@runCatching useCase.execute(Params(query = source.title)).toUIModel()
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
                        allSources = newSources
                    )
                )
            }
        }
    }

    suspend fun fetchNewsFrom(source: Source): UIModels {
        if (sourceWithUiMap.containsKey(source) && sourceWithUiMap[source] is SuccessUIModel) {
            return sourceWithUiMap[source]!!
        }

        try {
            sourceWithUiMap[source] = useCase.execute(Params(query = source.title)).toUIModel()
        } catch (e: Exception) {
            sourceWithUiMap[source] = ErrorUIModel(e, source)
        }

        return sourceWithUiMap[source]!!
    }
}

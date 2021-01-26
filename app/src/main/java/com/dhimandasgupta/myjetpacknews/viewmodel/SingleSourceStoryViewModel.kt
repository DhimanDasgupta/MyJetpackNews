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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SingleSourceStoryViewModel @Inject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {
    // Variables used for Single Source Story Composable
    private val newsUiStateMutableLiveData = MutableLiveData<NewsUiState>().also {
        it.value = initialNewsUiState
    }
    val newsUiState: LiveData<NewsUiState> = newsUiStateMutableLiveData
    private var currentJob: Job? = null

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
}

@Suppress("UNCHECKED_CAST")
class SingleSourceStoryViewModelFactory @Inject constructor(
    private val useCase: NewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleSourceStoryViewModel::class.java)) {
            return SingleSourceStoryViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

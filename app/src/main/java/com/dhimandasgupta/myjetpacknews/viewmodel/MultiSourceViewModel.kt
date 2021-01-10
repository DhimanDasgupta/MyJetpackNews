package com.dhimandasgupta.myjetpacknews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.Params
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.SuccessUIModel
import com.dhimandasgupta.data.presentaion.UIModels
import com.dhimandasgupta.data.presentaion.sources
import com.dhimandasgupta.data.presentaion.toUIModel
import java.lang.Exception

class MultiSourceViewModel(
    private val useCase: NewsUseCase
) : ViewModel() {
    // Variable used for Multi Source Composable
    val sourcesLiveData: LiveData<List<Source>> = MutableLiveData(sources)
    private val sourceWithUiMap = HashMap<Source, UIModels>()

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

@Suppress("UNCHECKED_CAST")
class MultiSourceViewModelFactory(
    private val useCase: NewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MultiSourceViewModel::class.java)) {
            return MultiSourceViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

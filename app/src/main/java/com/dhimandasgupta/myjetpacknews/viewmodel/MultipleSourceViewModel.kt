package com.dhimandasgupta.myjetpacknews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhimandasgupta.data.domain.NewsUseCase
import com.dhimandasgupta.data.domain.Params
import com.dhimandasgupta.data.presentaion.ErrorUIModel
import com.dhimandasgupta.data.presentaion.Source
import com.dhimandasgupta.data.presentaion.UIModels
import com.dhimandasgupta.data.presentaion.sources
import com.dhimandasgupta.data.presentaion.toUIModel
import java.lang.Exception

class MultipleSourceViewModel @ViewModelInject constructor(
    private val useCase: NewsUseCase
) : ViewModel() {
    val sourcesLiveData: LiveData<List<Source>> = MutableLiveData(sources)

    private val sourceWithUiMap = HashMap<Source, UIModels>()

    suspend fun fetchNewsFrom(source: Source): UIModels {
        if (sourceWithUiMap.containsKey(source)) {
            return sourceWithUiMap[source]!!
        }

        try {
            sourceWithUiMap[source] = useCase.getEverythingByQuery(Params(query = source.title)).toUIModel()
        } catch (e: Exception) {
            sourceWithUiMap[source] = ErrorUIModel(e, source)
        }

        return sourceWithUiMap[source]!!
    }
}

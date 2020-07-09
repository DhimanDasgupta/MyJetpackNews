package com.dhimandasgupta.data.presentaion

data class NewsUiState(
    val currentSource: Source,
    val uiModels: UIModels,
    val allSources: List<Source>
)

val initialNewsUiState = NewsUiState(
    currentSource = sources.first(),
    uiModels = IdleUIModel,
    allSources = sources
)
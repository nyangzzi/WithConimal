package com.nyangzzi.withconimal.presentation.feature.feed

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest

data class FeedUiState (
    val animals : List<AnimalInfo> = emptyList(),
    val selectData: AnimalInfo? = null,

    val totalCnt : Int = 0,
    val isShowImageExpand: Boolean = false,

    val isPickDate: Boolean = false,
    val request: SearchAnimalRequest = SearchAnimalRequest()
)
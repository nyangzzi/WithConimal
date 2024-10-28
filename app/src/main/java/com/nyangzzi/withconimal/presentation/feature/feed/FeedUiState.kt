package com.nyangzzi.withconimal.presentation.feature.feed

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.CareRoomInfo
import com.nyangzzi.withconimal.domain.model.common.CityInfo
import com.nyangzzi.withconimal.domain.model.common.TownInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest

data class FeedUiState (
    val animals : List<AnimalInfo> = emptyList(),
    val selectData: AnimalInfo? = null,
    val totalCnt : Int = 0,
    val isShowImageExpand: Boolean = false,
    val isShowFilter: Boolean = false,

    val selectCnt: Int = 0,

    val isPickDate: Boolean = false,
    val request: SearchAnimalRequest = SearchAnimalRequest(),

    val cityList: List<CityInfo>? = null,
    val townList: List<TownInfo>? = null,
    val careRoomList: List<CareRoomInfo>? = null,
)
package com.nyangzzi.withconimal.presentation.feature.feed

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.network.request.SearchAnimalRequest

sealed class FeedEvent {
    data class UpdateSelectInfo(val data: AnimalInfo?) : FeedEvent()
    data class UpdateRequest(val request: SearchAnimalRequest): FeedEvent()
    data object GetAnimalList : FeedEvent()
    data class SetShowImageExpand(val isShow: Boolean) : FeedEvent()
    data class SetShowFilter(val isShow: Boolean) : FeedEvent()
    data class AddFavoriteAnimal(val animalInfo: AnimalInfo): FeedEvent()
    data class DeleteFavoriteAnimal(val animalInfo: AnimalInfo): FeedEvent()
}
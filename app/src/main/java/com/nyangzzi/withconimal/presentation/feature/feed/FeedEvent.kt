package com.nyangzzi.withconimal.presentation.feature.feed

import com.nyangzzi.withconimal.domain.model.common.AnimalInfo

sealed class FeedEvent {
    data class UpdateSelectInfo(val data: AnimalInfo?) : FeedEvent()
    data class SetShowImageExpand(val isShow: Boolean) : FeedEvent()
    data class SetShowFilter(val isShow: Boolean) : FeedEvent()
}
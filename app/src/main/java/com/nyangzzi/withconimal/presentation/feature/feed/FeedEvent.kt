package com.nyangzzi.withconimal.presentation.feature.feed

sealed class FeedEvent {
    data class UpdateSelectIndex(val index: Int?) : FeedEvent()
}
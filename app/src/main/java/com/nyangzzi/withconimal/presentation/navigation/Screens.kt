package com.nyangzzi.withconimal.presentation.navigation

sealed class Screens(val route: String) {
    data object FeedDetailGraph: Screens(route = "feed_detail_graph")
    data object Feed: Screens(route = "feed")
    data object Detail: Screens(route = "detail")
}
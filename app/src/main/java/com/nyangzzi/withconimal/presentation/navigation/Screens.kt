package com.nyangzzi.withconimal.presentation.navigation

sealed class Screens(val route: String) {

    data object Parent: Screens(route = "parent")
    data object Feed: Screens(route = "feed")
    data object Detail: Screens(route = "detail")
    data object Favorite: Screens(route = "favorite")
}
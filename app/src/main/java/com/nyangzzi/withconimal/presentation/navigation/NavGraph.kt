package com.nyangzzi.withconimal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nyangzzi.withconimal.presentation.ui.screen.DetailScreen
import com.nyangzzi.withconimal.presentation.ui.screen.FeedScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Screens.Feed.route) {

        composable(route = Screens.Feed.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screens.Feed.route)
            }
            FeedScreen(
                navController = navController,
                viewModel = hiltViewModel(parentEntry)
            )
        }

        composable(route = Screens.Detail.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screens.Feed.route)
            }
            DetailScreen(
                navController = navController,
                viewModel = hiltViewModel(parentEntry)
            )
        }

//        navigation(startDestination = Screens.Feed.route, route = Screens.FeedDetailGraph.route) {
//
//
//            composable(route = Screens.Feed.route) {
//                val feedViewModel: FeedViewModel =
//                    hiltViewModel(navController.getBackStackEntry(Screens.FeedDetailGraph.route))
//                FeedScreen(
//                    navController = navController,
//                    viewModel = feedViewModel
//                )
//            }
//
//            composable(route = Screens.Detail.route) {
//                val feedViewModel: FeedViewModel =
//                    hiltViewModel(navController.getBackStackEntry(Screens.FeedDetailGraph.route))
//                DetailScreen(
//                    navController = navController,
//                    viewModel = feedViewModel
//                )
//            }
//        }
    }
}

private fun NavHostController.navigateSingleTopTo(route: String) {
    this.popBackStack()
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id,
        ) {
            //모든화면을 닫은 후 새 화면 열기
            inclusive = true
            //하단 탐색 항목 간 전환시 상태와 백 스택이 올바르게 저장
            saveState = true
        }
        //동일한 항목을 선택할때 여러번 복사를 방지
        launchSingleTop = true
        //하단 탐색 항목 간 전환시 상태와 백 스택이 복원
        restoreState = true
    }
}
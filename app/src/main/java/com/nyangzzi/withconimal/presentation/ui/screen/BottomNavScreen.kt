package com.nyangzzi.withconimal.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nyangzzi.withconimal.R
import com.nyangzzi.withconimal.presentation.navigation.Screens
import com.nyangzzi.withconimal.ui.theme.WithconimalTheme

@Composable
fun BottomNavScreen(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        Screens.Feed,
        Screens.Favorite
    )

    AnimatedVisibility(
        visible = items.map { it.route }.contains(currentRoute)
    ) {
        NavBar(
            navController = navController,
            currentRoute = currentRoute,
            items = items,
        )
    }
}

@Composable
private fun NavBar(
    navController: NavHostController,
    currentRoute: String?,
    items: List<Screens>
) {
    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                selected = currentRoute == item.route,
                icon = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(
                            id = if(currentRoute != item.route) {
                                when (item) {
                                    Screens.Feed -> R.drawable.ic_paw_line
                                    else -> R.drawable.ic_heart_line
                                }
                            }
                            else {
                                when (item) {
                                    Screens.Feed -> R.drawable.ic_paw_fill
                                    else -> R.drawable.ic_heart_fill
                                }
                            }
                        ),
                        contentDescription = ""
                    )
                },
                onClick = {
                    navController.navigate(item.route)
                    {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun Preview() {
    WithconimalTheme {
        NavBar(
            navController = rememberNavController(),
            currentRoute = Screens.Feed.route,
            items = listOf(
                Screens.Feed,
                Screens.Favorite
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewDark() {
    WithconimalTheme(darkTheme = true) {
        NavBar(
            navController = rememberNavController(),
            currentRoute = Screens.Feed.route,
            items = listOf(
                Screens.Feed,
                Screens.Favorite
            )
        )
    }
}
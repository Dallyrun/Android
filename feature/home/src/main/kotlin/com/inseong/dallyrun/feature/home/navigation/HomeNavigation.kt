package com.inseong.dallyrun.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.home.HomeRoute as HomeRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(onNavigateToRun: () -> Unit) {
    composable<HomeRoute> {
        HomeRouteScreen(onNavigateToRun = onNavigateToRun)
    }
}

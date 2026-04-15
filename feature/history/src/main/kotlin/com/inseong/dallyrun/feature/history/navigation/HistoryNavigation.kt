package com.inseong.dallyrun.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.history.HistoryRoute as HistoryRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object HistoryRoute

fun NavController.navigateToHistory(navOptions: NavOptions? = null) {
    navigate(HistoryRoute, navOptions)
}

fun NavGraphBuilder.historyScreen() {
    composable<HistoryRoute> {
        HistoryRouteScreen()
    }
}

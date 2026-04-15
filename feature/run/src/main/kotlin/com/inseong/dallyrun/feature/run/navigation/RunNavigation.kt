package com.inseong.dallyrun.feature.run.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.run.RunRoute as RunRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object RunRoute

fun NavController.navigateToRun(navOptions: NavOptions? = null) {
    navigate(RunRoute, navOptions)
}

fun NavGraphBuilder.runScreen() {
    composable<RunRoute> {
        RunRouteScreen()
    }
}

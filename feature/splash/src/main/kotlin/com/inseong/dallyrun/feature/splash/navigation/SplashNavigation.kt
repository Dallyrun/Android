package com.inseong.dallyrun.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.splash.SplashRoute as SplashRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(SplashRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable<SplashRoute> {
        SplashRouteScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToHome = onNavigateToHome,
        )
    }
}

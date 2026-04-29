package com.inseong.dallyrun.feature.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.my.MyRoute as MyRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyRoute

fun NavController.navigateToMy(navOptions: NavOptions? = null) {
    navigate(MyRoute, navOptions)
}

fun NavGraphBuilder.myScreen(
    onNavigateToLogin: () -> Unit,
) {
    composable<MyRoute> {
        MyRouteScreen(onNavigateToLogin = onNavigateToLogin)
    }
}

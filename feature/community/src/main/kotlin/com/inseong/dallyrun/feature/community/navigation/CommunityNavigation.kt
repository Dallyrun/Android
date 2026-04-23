package com.inseong.dallyrun.feature.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.inseong.dallyrun.feature.community.CommunityRoute as CommunityRouteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CommunityRoute

fun NavController.navigateToCommunity(navOptions: NavOptions? = null) {
    navigate(CommunityRoute, navOptions)
}

fun NavGraphBuilder.communityScreen() {
    composable<CommunityRoute> {
        CommunityRouteScreen()
    }
}

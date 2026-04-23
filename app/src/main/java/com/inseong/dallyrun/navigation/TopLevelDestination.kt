package com.inseong.dallyrun.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.inseong.dallyrun.R
import com.inseong.dallyrun.feature.community.navigation.CommunityRoute
import com.inseong.dallyrun.feature.history.navigation.HistoryRoute
import com.inseong.dallyrun.feature.home.navigation.HomeRoute
import com.inseong.dallyrun.feature.my.navigation.MyRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: Any,
    val routeClass: KClass<*>,
    @StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        route = HomeRoute,
        routeClass = HomeRoute::class,
        labelRes = R.string.bottom_nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    HISTORY(
        route = HistoryRoute,
        routeClass = HistoryRoute::class,
        labelRes = R.string.bottom_nav_history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
    ),
    COMMUNITY(
        route = CommunityRoute,
        routeClass = CommunityRoute::class,
        labelRes = R.string.bottom_nav_community,
        selectedIcon = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group,
    ),
    MY(
        route = MyRoute,
        routeClass = MyRoute::class,
        labelRes = R.string.bottom_nav_my,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}

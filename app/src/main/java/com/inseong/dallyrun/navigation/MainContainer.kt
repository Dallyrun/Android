package com.inseong.dallyrun.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inseong.dallyrun.feature.community.navigation.communityScreen
import com.inseong.dallyrun.feature.history.navigation.historyScreen
import com.inseong.dallyrun.feature.home.navigation.HomeRoute
import com.inseong.dallyrun.feature.home.navigation.homeScreen
import com.inseong.dallyrun.feature.my.navigation.myScreen

@Composable
internal fun MainContainer(
    onNavigateToRun: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            DallyrunBottomBar(
                destinations = TopLevelDestination.entries,
                currentDestination = currentDestination,
                onNavigate = { dest ->
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            homeScreen(onNavigateToRun = onNavigateToRun)
            historyScreen()
            communityScreen()
            myScreen(onNavigateToLogin = onNavigateToLogin)
        }
    }
}

package com.inseong.dallyrun.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.inseong.dallyrun.feature.history.navigation.historyScreen
import com.inseong.dallyrun.feature.run.navigation.RunRoute
import com.inseong.dallyrun.feature.run.navigation.runScreen

@Composable
fun DallyrunNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = RunRoute,
        modifier = modifier,
    ) {
        runScreen()
        historyScreen()
    }
}

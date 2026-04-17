package com.inseong.dallyrun.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.inseong.dallyrun.feature.history.navigation.historyScreen
import com.inseong.dallyrun.feature.login.navigation.LoginRoute
import com.inseong.dallyrun.feature.login.navigation.loginScreen
import com.inseong.dallyrun.feature.run.navigation.runScreen

@Composable
fun DallyrunNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier,
    ) {
        loginScreen()
        runScreen()
        historyScreen()
    }
}

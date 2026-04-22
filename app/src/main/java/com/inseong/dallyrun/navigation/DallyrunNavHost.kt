package com.inseong.dallyrun.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.inseong.dallyrun.feature.history.navigation.historyScreen
import com.inseong.dallyrun.feature.login.navigation.LoginRoute
import com.inseong.dallyrun.feature.login.navigation.loginScreen
import com.inseong.dallyrun.feature.run.navigation.RunRoute
import com.inseong.dallyrun.feature.run.navigation.runScreen
import com.inseong.dallyrun.feature.signup.navigation.navigateToSignup
import com.inseong.dallyrun.feature.signup.navigation.signupGraph

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
        loginScreen(
            onNavigateToSignup = { navController.navigateToSignup() },
            onNavigateToHome = { navController.navigateToHomeFromAuth() },
        )
        signupGraph(
            navController = navController,
            onSignupComplete = { navController.navigateToHomeFromAuth() },
        )
        runScreen()
        historyScreen()
    }
}

private fun NavHostController.navigateToHomeFromAuth() {
    navigate(
        route = RunRoute,
        navOptions = navOptions {
            popUpTo(LoginRoute) { inclusive = true }
        },
    )
}

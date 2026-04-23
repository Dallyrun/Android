package com.inseong.dallyrun.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
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
            onNavigateToHome = { navController.navigateToMainFromAuth() },
        )
        signupGraph(
            navController = navController,
            onSignupComplete = { navController.navigateToMainFromAuth() },
        )
        composable<MainRoute> {
            MainContainer(
                onNavigateToRun = { navController.navigate(RunRoute) },
            )
        }
        runScreen()
    }
}

private fun NavHostController.navigateToMainFromAuth() {
    navigate(
        route = MainRoute,
        navOptions = navOptions {
            popUpTo(LoginRoute) { inclusive = true }
        },
    )
}

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
import com.inseong.dallyrun.feature.splash.navigation.SplashRoute
import com.inseong.dallyrun.feature.splash.navigation.splashScreen

@Composable
fun DallyrunNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
    ) {
        splashScreen(
            onNavigateToLogin = { navController.navigateClearingSplash(LoginRoute) },
            onNavigateToHome = { navController.navigateClearingSplash(MainRoute) },
        )
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
                onNavigateToLogin = { navController.navigateToLoginClearingStack() },
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

private fun NavHostController.navigateClearingSplash(route: Any) {
    navigate(
        route = route,
        navOptions = navOptions {
            popUpTo(SplashRoute) { inclusive = true }
        },
    )
}

private fun NavHostController.navigateToLoginClearingStack() {
    navigate(
        route = LoginRoute,
        navOptions = navOptions {
            popUpTo(graph.id) { inclusive = true }
        },
    )
}

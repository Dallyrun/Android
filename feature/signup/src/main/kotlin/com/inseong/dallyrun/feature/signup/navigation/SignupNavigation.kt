package com.inseong.dallyrun.feature.signup.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.inseong.dallyrun.feature.signup.SignupEmailRoute
import com.inseong.dallyrun.feature.signup.SignupPasswordRoute
import com.inseong.dallyrun.feature.signup.SignupProfileRoute
import com.inseong.dallyrun.feature.signup.SignupViewModel
import kotlinx.serialization.Serializable

@Serializable
data object SignupGraph

@Serializable
internal data object SignupEmailScreenRoute

@Serializable
internal data object SignupPasswordScreenRoute

@Serializable
internal data object SignupProfileScreenRoute

fun NavController.navigateToSignup(navOptions: NavOptions? = null) {
    navigate(SignupGraph, navOptions)
}

fun NavGraphBuilder.signupGraph(
    navController: NavController,
    onSignupComplete: () -> Unit,
) {
    navigation<SignupGraph>(startDestination = SignupEmailScreenRoute) {
        composable<SignupEmailScreenRoute> { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(SignupGraph)
            }
            val viewModel: SignupViewModel = hiltViewModel(parentEntry)
            SignupEmailRoute(
                viewModel = viewModel,
                onNext = { navController.navigate(SignupPasswordScreenRoute) },
            )
        }

        composable<SignupPasswordScreenRoute> { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(SignupGraph)
            }
            val viewModel: SignupViewModel = hiltViewModel(parentEntry)
            SignupPasswordRoute(
                viewModel = viewModel,
                onNext = { navController.navigate(SignupProfileScreenRoute) },
            )
        }

        composable<SignupProfileScreenRoute> { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(SignupGraph)
            }
            val viewModel: SignupViewModel = hiltViewModel(parentEntry)
            SignupProfileRoute(
                viewModel = viewModel,
                onSignupComplete = onSignupComplete,
            )
        }
    }
}

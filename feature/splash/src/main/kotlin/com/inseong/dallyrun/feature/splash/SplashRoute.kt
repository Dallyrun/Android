package com.inseong.dallyrun.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SplashRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToLogin -> onNavigateToLogin()
                SplashSideEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    SplashScreen(modifier = modifier)
}

package com.inseong.dallyrun.feature.splash

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data object SplashUiState : UiState

sealed interface SplashUiEvent : UiEvent

sealed interface SplashSideEffect : SideEffect {
    data object NavigateToLogin : SplashSideEffect
    data object NavigateToHome : SplashSideEffect
}

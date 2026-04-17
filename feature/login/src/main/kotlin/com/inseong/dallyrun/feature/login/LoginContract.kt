package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class LoginUiState(
    val isLoading: Boolean = false,
) : UiState

sealed interface LoginUiEvent : UiEvent {
    data object OnKakaoLoginClick : LoginUiEvent
}

sealed interface LoginSideEffect : SideEffect {
    data object LaunchKakaoLogin : LoginSideEffect
    data class ShowError(val message: String) : LoginSideEffect
}

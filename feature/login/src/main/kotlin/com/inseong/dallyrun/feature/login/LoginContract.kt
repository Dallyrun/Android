package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {

    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}

sealed interface LoginUiEvent : UiEvent {
    data class OnEmailChange(val value: String) : LoginUiEvent
    data class OnPasswordChange(val value: String) : LoginUiEvent
    data object OnPasswordVisibilityToggle : LoginUiEvent
    data object OnLoginClick : LoginUiEvent
    data object OnSignupClick : LoginUiEvent
    data object OnErrorDismiss : LoginUiEvent
}

sealed interface LoginSideEffect : SideEffect {
    data object NavigateToSignup : LoginSideEffect
    data object NavigateToHome : LoginSideEffect
}

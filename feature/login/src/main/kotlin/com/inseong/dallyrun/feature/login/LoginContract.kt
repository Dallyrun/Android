package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState
import com.inseong.dallyrun.core.domain.auth.isValidEmail
import com.inseong.dallyrun.core.domain.auth.isValidPassword

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {

    /** 빈 칸일 땐 true (아직 입력 안 했으니 에러 안 띄움), 입력했으면 형식 OK 여부 */
    val isEmailValid: Boolean
        get() = email.isEmpty() || isValidEmail(email)

    val isPasswordValid: Boolean
        get() = password.isEmpty() || isValidPassword(password)

    val canSubmit: Boolean
        get() = isValidEmail(email) && isValidPassword(password) && !isLoading
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

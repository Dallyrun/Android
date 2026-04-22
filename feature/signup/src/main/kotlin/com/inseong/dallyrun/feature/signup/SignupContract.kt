package com.inseong.dallyrun.feature.signup

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState
import com.inseong.dallyrun.core.domain.auth.isValidEmail
import com.inseong.dallyrun.core.domain.auth.isValidPassword

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val nickname: String = "",
    val profileImageUri: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {

    val canProceedFromEmail: Boolean
        get() = isValidEmail(email)

    val canProceedFromPassword: Boolean
        get() = isValidPassword(password) && password == passwordConfirm

    val canSubmit: Boolean
        get() = nickname.isNotBlank() && !isLoading
}

sealed interface SignupUiEvent : UiEvent {
    data class OnEmailChange(val value: String) : SignupUiEvent
    data class OnPasswordChange(val value: String) : SignupUiEvent
    data class OnPasswordConfirmChange(val value: String) : SignupUiEvent
    data class OnNicknameChange(val value: String) : SignupUiEvent
    data class OnProfileImageSelected(val uri: String?) : SignupUiEvent
    data object OnSubmit : SignupUiEvent
    data object OnErrorDismiss : SignupUiEvent
}

sealed interface SignupSideEffect : SideEffect {
    data object NavigateToHome : SignupSideEffect
}

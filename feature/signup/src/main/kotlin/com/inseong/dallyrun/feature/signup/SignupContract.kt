package com.inseong.dallyrun.feature.signup

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState
import com.inseong.dallyrun.core.domain.auth.isValidEmail
import com.inseong.dallyrun.core.domain.auth.isValidNickname
import com.inseong.dallyrun.core.domain.auth.isValidPassword
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.Gender

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val profileImageUri: String? = null,
    val nickname: String = "",
    val ageGroup: AgeGroup? = null,
    val gender: Gender? = null,
    val isAgeDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {

    val canProceedFromEmail: Boolean
        get() = isValidEmail(email)

    val canProceedFromPassword: Boolean
        get() = isValidPassword(password) && password == passwordConfirm

    val canSubmit: Boolean
        get() = isValidNickname(nickname) &&
            ageGroup != null &&
            gender != null &&
            !isLoading
}

sealed interface SignupUiEvent : UiEvent {
    data class OnEmailChange(val value: String) : SignupUiEvent
    data class OnPasswordChange(val value: String) : SignupUiEvent
    data class OnPasswordConfirmChange(val value: String) : SignupUiEvent
    data class OnNicknameChange(val value: String) : SignupUiEvent
    data class OnProfileImageSelected(val uri: String?) : SignupUiEvent
    data class OnAgeGroupSelect(val value: AgeGroup) : SignupUiEvent
    data object OnAgeDropdownExpand : SignupUiEvent
    data object OnAgeDropdownDismiss : SignupUiEvent
    data class OnGenderSelect(val value: Gender) : SignupUiEvent
    data object OnSubmit : SignupUiEvent
    data object OnErrorDismiss : SignupUiEvent
}

sealed interface SignupSideEffect : SideEffect {
    data object NavigateToHome : SignupSideEffect
}

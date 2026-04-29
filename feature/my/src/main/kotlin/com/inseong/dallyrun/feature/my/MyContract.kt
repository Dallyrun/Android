package com.inseong.dallyrun.feature.my

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class MyUiState(
    val isLogoutDialogVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val deletePasswordInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canSubmitDelete: Boolean
        get() = deletePasswordInput.isNotBlank() && !isLoading
}

sealed interface MyUiEvent : UiEvent {
    data object OnLogoutClick : MyUiEvent
    data object OnLogoutConfirm : MyUiEvent
    data object OnLogoutDismiss : MyUiEvent
    data object OnDeleteAccountClick : MyUiEvent
    data class OnDeletePasswordChange(val value: String) : MyUiEvent
    data object OnDeleteConfirm : MyUiEvent
    data object OnDeleteDismiss : MyUiEvent
    data object OnErrorDismiss : MyUiEvent
}

sealed interface MySideEffect : SideEffect {
    data object NavigateToLogin : MySideEffect
}

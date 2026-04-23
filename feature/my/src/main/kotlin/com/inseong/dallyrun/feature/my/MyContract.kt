package com.inseong.dallyrun.feature.my

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class MyUiState(
    val isLoading: Boolean = false,
) : UiState

sealed interface MyUiEvent : UiEvent

sealed interface MySideEffect : SideEffect

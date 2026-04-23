package com.inseong.dallyrun.feature.home

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class HomeUiState(
    val isLoading: Boolean = false,
) : UiState

sealed interface HomeUiEvent : UiEvent {
    data object OnStartRunClick : HomeUiEvent
}

sealed interface HomeSideEffect : SideEffect {
    data object NavigateToRun : HomeSideEffect
}

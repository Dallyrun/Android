package com.inseong.dallyrun.feature.community

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class CommunityUiState(
    val isLoading: Boolean = false,
) : UiState

sealed interface CommunityUiEvent : UiEvent

sealed interface CommunitySideEffect : SideEffect

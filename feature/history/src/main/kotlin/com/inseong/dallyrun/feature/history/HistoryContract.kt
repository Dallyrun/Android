package com.inseong.dallyrun.feature.history

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState
import com.inseong.dallyrun.core.model.Run

data class HistoryUiState(
    val isLoading: Boolean = false,
    val runs: List<Run> = emptyList(),
) : UiState

sealed interface HistoryUiEvent : UiEvent {
    data object LoadHistory : HistoryUiEvent
    data class SelectRun(val runId: Long) : HistoryUiEvent
}

sealed interface HistorySideEffect : SideEffect {
    data class NavigateToRunDetail(val runId: Long) : HistorySideEffect
}

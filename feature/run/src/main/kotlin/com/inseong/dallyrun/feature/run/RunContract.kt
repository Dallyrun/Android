package com.inseong.dallyrun.feature.run

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class RunUiState(
    val isRunning: Boolean = false,
    val distanceMeters: Double = 0.0,
    val durationMillis: Long = 0L,
    val paceMinPerKm: Double = 0.0,
) : UiState

sealed interface RunUiEvent : UiEvent {
    data object StartRun : RunUiEvent
    data object PauseRun : RunUiEvent
    data object StopRun : RunUiEvent
}

sealed interface RunSideEffect : SideEffect {
    data object NavigateToHistory : RunSideEffect
    data class ShowError(val message: String) : RunSideEffect
}

package com.inseong.dallyrun.feature.run

import com.inseong.dallyrun.core.common.mvi.SideEffect
import com.inseong.dallyrun.core.common.mvi.UiEvent
import com.inseong.dallyrun.core.common.mvi.UiState

data class RunUiState(
    val isRunning: Boolean = false,
    val distanceMeters: Double = 0.0,
    val durationMillis: Long = 0L,
    val locationPermissionGranted: Boolean = false,
    val hasRequestedPermission: Boolean = false,
) : UiState

sealed interface RunUiEvent : UiEvent {
    data object StartRun : RunUiEvent
    data object PauseRun : RunUiEvent
    data object StopRun : RunUiEvent
    data class OnPermissionStateChanged(val granted: Boolean) : RunUiEvent
    data object RequestPermission : RunUiEvent
    data class OnPermissionResult(val granted: Boolean) : RunUiEvent
    data object OpenAppSettings : RunUiEvent
}

sealed interface RunSideEffect : SideEffect {
    data object NavigateToHistory : RunSideEffect
    data object LaunchPermissionRequest : RunSideEffect
    data object OpenAppSettings : RunSideEffect
}

package com.inseong.dallyrun.feature.run

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor() : DallyrunViewModel<RunUiState, RunUiEvent, RunSideEffect>() {

    override fun createInitialState(): RunUiState = RunUiState()

    override fun handleEvent(event: RunUiEvent) {
        when (event) {
            RunUiEvent.StartRun -> updateState { copy(isRunning = true) }
            RunUiEvent.PauseRun -> updateState { copy(isRunning = false) }
            RunUiEvent.StopRun -> {
                updateState { copy(isRunning = false, distanceMeters = 0.0, durationMillis = 0L) }
                sendSideEffect(RunSideEffect.NavigateToHistory)
            }
            is RunUiEvent.OnPermissionStateChanged -> updateState {
                copy(locationPermissionGranted = event.granted)
            }
            RunUiEvent.RequestPermission -> sendSideEffect(RunSideEffect.LaunchPermissionRequest)
            is RunUiEvent.OnPermissionResult -> updateState {
                copy(locationPermissionGranted = event.granted, hasRequestedPermission = true)
            }
            RunUiEvent.OpenAppSettings -> sendSideEffect(RunSideEffect.OpenAppSettings)
        }
    }
}

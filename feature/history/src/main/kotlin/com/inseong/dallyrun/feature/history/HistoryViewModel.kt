package com.inseong.dallyrun.feature.history

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : DallyrunViewModel<HistoryUiState, HistoryUiEvent, HistorySideEffect>() {

    override fun createInitialState(): HistoryUiState = HistoryUiState()

    override fun handleEvent(event: HistoryUiEvent) {
        when (event) {
            HistoryUiEvent.LoadHistory -> {
                updateState { copy(isLoading = true) }
                // TODO: Load history from repository
            }
            is HistoryUiEvent.SelectRun -> {
                sendSideEffect(HistorySideEffect.NavigateToRunDetail(event.runId))
            }
        }
    }
}

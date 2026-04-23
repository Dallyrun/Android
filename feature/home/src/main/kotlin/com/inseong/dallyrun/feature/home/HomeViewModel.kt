package com.inseong.dallyrun.feature.home

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : DallyrunViewModel<HomeUiState, HomeUiEvent, HomeSideEffect>() {

    override fun createInitialState(): HomeUiState = HomeUiState()

    override fun handleEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnStartRunClick -> sendSideEffect(HomeSideEffect.NavigateToRun)
        }
    }
}

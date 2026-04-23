package com.inseong.dallyrun.feature.community

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor() :
    DallyrunViewModel<CommunityUiState, CommunityUiEvent, CommunitySideEffect>() {

    override fun createInitialState(): CommunityUiState = CommunityUiState()

    override fun handleEvent(event: CommunityUiEvent) {
        // TODO: 이벤트 추가 예정
    }
}

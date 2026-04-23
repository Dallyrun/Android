package com.inseong.dallyrun.feature.my

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor() : DallyrunViewModel<MyUiState, MyUiEvent, MySideEffect>() {

    override fun createInitialState(): MyUiState = MyUiState()

    override fun handleEvent(event: MyUiEvent) {
        // TODO: 이벤트 추가 예정
    }
}

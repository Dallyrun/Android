package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : DallyrunViewModel<LoginUiState, LoginUiEvent, LoginSideEffect>() {

    override fun createInitialState(): LoginUiState = LoginUiState()

    override fun handleEvent(event: LoginUiEvent) {
        // TODO: 이메일/비밀번호 로그인 이벤트 추가 예정
    }
}

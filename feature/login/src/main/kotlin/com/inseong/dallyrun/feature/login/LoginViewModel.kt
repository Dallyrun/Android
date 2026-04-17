package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : DallyrunViewModel<LoginUiState, LoginUiEvent, LoginSideEffect>() {

    override fun createInitialState(): LoginUiState = LoginUiState()

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            LoginUiEvent.OnKakaoLoginClick -> sendSideEffect(LoginSideEffect.LaunchKakaoLogin)
        }
    }
}

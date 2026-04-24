package com.inseong.dallyrun.feature.splash

import androidx.lifecycle.viewModelScope
import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import com.inseong.dallyrun.core.domain.auth.GetLoginStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getLoginState: GetLoginStateUseCase,
) : DallyrunViewModel<SplashUiState, SplashUiEvent, SplashSideEffect>() {

    init {
        decideRoute()
    }

    override fun createInitialState(): SplashUiState = SplashUiState

    override fun handleEvent(event: SplashUiEvent) {
        // 이벤트 없음
    }

    private fun decideRoute() = viewModelScope.launch {
        // 토큰 확인과 최소 노출 시간을 병렬로 실행 → 두 조건 모두 만족 후 분기
        val loggedInDeferred = async { getLoginState().first() }
        delay(SPLASH_DURATION_MS)
        val loggedIn = loggedInDeferred.await()
        sendSideEffect(
            if (loggedIn) {
                SplashSideEffect.NavigateToHome
            } else {
                SplashSideEffect.NavigateToLogin
            },
        )
    }

    private companion object {
        const val SPLASH_DURATION_MS = 1500L
    }
}

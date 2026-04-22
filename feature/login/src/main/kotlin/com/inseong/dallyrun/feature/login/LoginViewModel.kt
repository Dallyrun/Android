package com.inseong.dallyrun.feature.login

import androidx.lifecycle.viewModelScope
import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import com.inseong.dallyrun.core.domain.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : DallyrunViewModel<LoginUiState, LoginUiEvent, LoginSideEffect>() {

    override fun createInitialState(): LoginUiState = LoginUiState()

    override fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChange ->
                updateState { copy(email = event.value, errorMessage = null) }

            is LoginUiEvent.OnPasswordChange ->
                updateState { copy(password = event.value, errorMessage = null) }

            LoginUiEvent.OnPasswordVisibilityToggle ->
                updateState { copy(isPasswordVisible = !isPasswordVisible) }

            LoginUiEvent.OnLoginClick -> attemptLogin()

            LoginUiEvent.OnSignupClick -> sendSideEffect(LoginSideEffect.NavigateToSignup)

            LoginUiEvent.OnErrorDismiss -> updateState { copy(errorMessage = null) }
        }
    }

    private fun attemptLogin() {
        val state = uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            try {
                loginUseCase(state.email, state.password)
                sendSideEffect(LoginSideEffect.NavigateToHome)
            } catch (cancel: CancellationException) {
                throw cancel
            } catch (throwable: Throwable) {
                updateState { copy(errorMessage = throwable.message ?: "로그인에 실패했어요") }
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
}

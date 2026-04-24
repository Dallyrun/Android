package com.inseong.dallyrun.feature.signup

import androidx.lifecycle.viewModelScope
import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import com.inseong.dallyrun.core.domain.auth.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
) : DallyrunViewModel<SignupUiState, SignupUiEvent, SignupSideEffect>() {

    override fun createInitialState(): SignupUiState = SignupUiState()

    override fun handleEvent(event: SignupUiEvent) {
        when (event) {
            is SignupUiEvent.OnEmailChange ->
                updateState { copy(email = event.value, errorMessage = null) }

            is SignupUiEvent.OnPasswordChange ->
                updateState { copy(password = event.value, errorMessage = null) }

            is SignupUiEvent.OnPasswordConfirmChange ->
                updateState { copy(passwordConfirm = event.value, errorMessage = null) }

            is SignupUiEvent.OnNicknameChange ->
                updateState { copy(nickname = event.value, errorMessage = null) }

            is SignupUiEvent.OnProfileImageSelected ->
                updateState { copy(profileImageUri = event.uri) }

            is SignupUiEvent.OnAgeGroupSelect ->
                updateState {
                    copy(
                        ageGroup = event.value,
                        isAgeDropdownExpanded = false,
                        errorMessage = null,
                    )
                }

            SignupUiEvent.OnAgeDropdownExpand ->
                updateState { copy(isAgeDropdownExpanded = true) }

            SignupUiEvent.OnAgeDropdownDismiss ->
                updateState { copy(isAgeDropdownExpanded = false) }

            is SignupUiEvent.OnGenderSelect ->
                updateState { copy(gender = event.value, errorMessage = null) }

            SignupUiEvent.OnSubmit -> attemptSignup()

            SignupUiEvent.OnErrorDismiss -> updateState { copy(errorMessage = null) }
        }
    }

    private fun attemptSignup() {
        val state = uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            try {
                signupUseCase(
                    email = state.email,
                    password = state.password,
                    nickname = state.nickname,
                    profileImageUri = state.profileImageUri,
                    ageGroup = requireNotNull(state.ageGroup),
                    gender = requireNotNull(state.gender),
                )
                sendSideEffect(SignupSideEffect.NavigateToHome)
            } catch (cancel: CancellationException) {
                throw cancel
            } catch (throwable: Throwable) {
                updateState { copy(errorMessage = throwable.message ?: "회원가입에 실패했어요") }
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
}

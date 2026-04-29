package com.inseong.dallyrun.feature.my

import androidx.lifecycle.viewModelScope
import com.inseong.dallyrun.core.common.mvi.DallyrunViewModel
import com.inseong.dallyrun.core.domain.auth.DeleteMemberUseCase
import com.inseong.dallyrun.core.domain.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
) : DallyrunViewModel<MyUiState, MyUiEvent, MySideEffect>() {

    override fun createInitialState(): MyUiState = MyUiState()

    override fun handleEvent(event: MyUiEvent) {
        when (event) {
            MyUiEvent.OnLogoutClick ->
                updateState { copy(isLogoutDialogVisible = true) }

            MyUiEvent.OnLogoutDismiss ->
                updateState { copy(isLogoutDialogVisible = false) }

            MyUiEvent.OnLogoutConfirm -> performLogout()

            MyUiEvent.OnDeleteAccountClick ->
                updateState {
                    copy(
                        isDeleteDialogVisible = true,
                        deletePasswordInput = "",
                        errorMessage = null,
                    )
                }

            is MyUiEvent.OnDeletePasswordChange ->
                updateState { copy(deletePasswordInput = event.value, errorMessage = null) }

            MyUiEvent.OnDeleteDismiss ->
                updateState {
                    copy(
                        isDeleteDialogVisible = false,
                        deletePasswordInput = "",
                        errorMessage = null,
                    )
                }

            MyUiEvent.OnDeleteConfirm -> performDeleteAccount()

            MyUiEvent.OnErrorDismiss ->
                updateState { copy(errorMessage = null) }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isLogoutDialogVisible = false) }
            try {
                logoutUseCase()
            } catch (cancel: CancellationException) {
                throw cancel
            } catch (_: Throwable) {
                // Repository 가 finally 에서 토큰을 삭제하므로 네트워크 실패해도 로그인 화면으로 이동
            } finally {
                updateState { copy(isLoading = false) }
            }
            sendSideEffect(MySideEffect.NavigateToLogin)
        }
    }

    private fun performDeleteAccount() {
        val state = uiState.value
        if (!state.canSubmitDelete) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            try {
                deleteMemberUseCase(state.deletePasswordInput)
                updateState {
                    copy(
                        isDeleteDialogVisible = false,
                        deletePasswordInput = "",
                    )
                }
                sendSideEffect(MySideEffect.NavigateToLogin)
            } catch (cancel: CancellationException) {
                throw cancel
            } catch (throwable: Throwable) {
                updateState {
                    copy(errorMessage = throwable.message ?: "탈퇴에 실패했어요")
                }
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }
}

package com.inseong.dallyrun.feature.login

import app.cash.turbine.test
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should keep initial state on creation`() {
        val viewModel = LoginViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
    }

    @Test
    fun `should emit LaunchKakaoLogin side effect when OnKakaoLoginClick`() = runTest {
        val viewModel = LoginViewModel()

        viewModel.sideEffect.test {
            viewModel.onEvent(LoginUiEvent.OnKakaoLoginClick)

            assertEquals(LoginSideEffect.LaunchKakaoLogin, awaitItem())
        }
    }
}

package com.inseong.dallyrun.feature.login

import app.cash.turbine.test
import com.inseong.dallyrun.core.domain.auth.LoginUseCase
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase = mockk<LoginUseCase>()
    private fun viewModel() = LoginViewModel(loginUseCase)

    @Test
    fun `should keep canSubmit false when fields are blank`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should enable canSubmit when both email and password are filled`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345678"))
        assertTrue(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should toggle password visibility`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isPasswordVisible)
        vm.onEvent(LoginUiEvent.OnPasswordVisibilityToggle)
        assertTrue(vm.uiState.value.isPasswordVisible)
    }

    @Test
    fun `should emit NavigateToSignup on signup click`() = runTest {
        val vm = viewModel()
        vm.sideEffect.test {
            vm.onEvent(LoginUiEvent.OnSignupClick)
            assertEquals(LoginSideEffect.NavigateToSignup, awaitItem())
        }
    }

    @Test
    fun `should emit NavigateToHome on successful login`() = runTest {
        coEvery { loginUseCase("a@b.c", "12345678") } returns
            AuthToken(accessToken = "access", refreshToken = "refresh")

        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345678"))

        vm.sideEffect.test {
            vm.onEvent(LoginUiEvent.OnLoginClick)
            assertEquals(LoginSideEffect.NavigateToHome, awaitItem())
        }

        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should set errorMessage on login failure`() = runTest {
        coEvery { loginUseCase(any(), any()) } throws RuntimeException("invalid credentials")

        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345678"))
        vm.onEvent(LoginUiEvent.OnLoginClick)

        assertEquals("invalid credentials", vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should clear errorMessage on dismiss`() = runTest {
        coEvery { loginUseCase(any(), any()) } throws RuntimeException("boom")

        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345678"))
        vm.onEvent(LoginUiEvent.OnLoginClick)
        assertNotNull(vm.uiState.value.errorMessage)

        vm.onEvent(LoginUiEvent.OnErrorDismiss)
        assertEquals(null, vm.uiState.value.errorMessage)
    }
}

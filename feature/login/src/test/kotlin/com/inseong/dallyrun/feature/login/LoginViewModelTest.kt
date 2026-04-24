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

    // ───── canSubmit / 형식 검증 ─────

    @Test
    fun `should keep canSubmit false when fields are blank`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should reject canSubmit when email format invalid`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("not-an-email"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("Aa1!Aa1!"))
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should reject canSubmit when password format invalid`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345"))
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should enable canSubmit when both fields are valid`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("Aa1!Aa1!"))
        assertTrue(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should treat blank input as valid (no isError flag)`() {
        val vm = viewModel()
        // 빈 칸이면 isXxxValid 는 true (입력 안 했으니 에러 아님)
        assertTrue(vm.uiState.value.isEmailValid)
        assertTrue(vm.uiState.value.isPasswordValid)
    }

    @Test
    fun `should mark email invalid when format wrong`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("not-email"))
        assertFalse(vm.uiState.value.isEmailValid)
    }

    @Test
    fun `should mark password invalid when format wrong`() {
        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnPasswordChange("12345"))
        assertFalse(vm.uiState.value.isPasswordValid)
    }

    // ───── 비밀번호 보기 토글 ─────

    @Test
    fun `should toggle password visibility`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isPasswordVisible)
        vm.onEvent(LoginUiEvent.OnPasswordVisibilityToggle)
        assertTrue(vm.uiState.value.isPasswordVisible)
    }

    // ───── SideEffect ─────

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
        coEvery { loginUseCase("a@b.c", "Aa1!Aa1!") } returns
            AuthToken(accessToken = "access", refreshToken = "refresh")

        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("Aa1!Aa1!"))

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
        vm.onEvent(LoginUiEvent.OnPasswordChange("Aa1!Aa1!"))
        vm.onEvent(LoginUiEvent.OnLoginClick)

        assertEquals("invalid credentials", vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should clear errorMessage on dismiss`() = runTest {
        coEvery { loginUseCase(any(), any()) } throws RuntimeException("boom")

        val vm = viewModel()
        vm.onEvent(LoginUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(LoginUiEvent.OnPasswordChange("Aa1!Aa1!"))
        vm.onEvent(LoginUiEvent.OnLoginClick)
        assertNotNull(vm.uiState.value.errorMessage)

        vm.onEvent(LoginUiEvent.OnErrorDismiss)
        assertEquals(null, vm.uiState.value.errorMessage)
    }
}

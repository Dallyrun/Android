package com.inseong.dallyrun.feature.signup

import app.cash.turbine.test
import com.inseong.dallyrun.core.domain.auth.SignupUseCase
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SignupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val signupUseCase = mockk<SignupUseCase>()
    private fun viewModel() = SignupViewModel(signupUseCase)

    // ───── Email step ─────

    @Test
    fun `should reject invalid email`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnEmailChange("not-an-email"))
        assertFalse(vm.uiState.value.canProceedFromEmail)
    }

    @Test
    fun `should accept valid email`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnEmailChange("a@b.c"))
        assertTrue(vm.uiState.value.canProceedFromEmail)
    }

    // ───── Password step ─────

    @Test
    fun `should reject password length 7`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("a".repeat(7)))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("a".repeat(7)))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should accept password length 8 when confirm matches`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("a".repeat(8)))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("a".repeat(8)))
        assertTrue(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject password length 101`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("a".repeat(101)))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("a".repeat(101)))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject when password and confirm mismatch`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("12345678"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("12345679"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    // ───── Profile step ─────

    @Test
    fun `should enable canSubmit when nickname is filled`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        assertTrue(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should store profile image uri`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnProfileImageSelected("content://image/123"))
        assertEquals("content://image/123", vm.uiState.value.profileImageUri)
    }

    // ───── Submit ─────

    @Test
    fun `should emit NavigateToHome on successful signup`() = runTest {
        coEvery { signupUseCase(any(), any(), any(), any()) } returns
            AuthToken(accessToken = "a", refreshToken = "r")

        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(SignupUiEvent.OnPasswordChange("12345678"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("12345678"))
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))

        vm.sideEffect.test {
            vm.onEvent(SignupUiEvent.OnSubmit)
            assertEquals(SignupSideEffect.NavigateToHome, awaitItem())
        }

        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should set errorMessage on signup failure`() = runTest {
        coEvery { signupUseCase(any(), any(), any(), any()) } throws
            RuntimeException("backend not ready")

        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        vm.onEvent(SignupUiEvent.OnSubmit)

        assertEquals("backend not ready", vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should clear errorMessage on dismiss`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnEmailChange("a@b.c"))
        // simulate error existed
        vm.onEvent(SignupUiEvent.OnErrorDismiss)
        assertNull(vm.uiState.value.errorMessage)
    }
}

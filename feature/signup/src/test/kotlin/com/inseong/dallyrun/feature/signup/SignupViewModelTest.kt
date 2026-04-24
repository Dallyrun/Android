package com.inseong.dallyrun.feature.signup

import app.cash.turbine.test
import com.inseong.dallyrun.core.domain.auth.SignupUseCase
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
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
    fun `should reject password length 7 even with all classes`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1!Aa1"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1!Aa1"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should accept password length 8 when confirm matches`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1!Aa1!"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1!Aa1!"))
        assertTrue(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject password length 31`() {
        val vm = viewModel()
        val long = "Aa1!" + "a".repeat(27)
        vm.onEvent(SignupUiEvent.OnPasswordChange(long))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange(long))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject password without special character`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aabc1234"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aabc1234"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject password with whitespace`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1! aaa"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1! aaa"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject password with Korean character`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1!한aa"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1!한aa"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    @Test
    fun `should reject when password and confirm mismatch`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1!aaaa"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1!aaab"))
        assertFalse(vm.uiState.value.canProceedFromPassword)
    }

    // ───── Profile step ─────

    @Test
    fun `should reject canSubmit when only nickname is filled`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should reject canSubmit when nickname is single character`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("r"))
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.THIRTIES))
        vm.onEvent(SignupUiEvent.OnGenderSelect(Gender.MALE))
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should reject canSubmit when nickname has whitespace`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("run ner"))
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.THIRTIES))
        vm.onEvent(SignupUiEvent.OnGenderSelect(Gender.MALE))
        assertFalse(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should enable canSubmit when nickname age and gender are all set`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.THIRTIES))
        vm.onEvent(SignupUiEvent.OnGenderSelect(Gender.MALE))
        assertTrue(vm.uiState.value.canSubmit)
    }

    @Test
    fun `should store profile image uri`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnProfileImageSelected("content://image/123"))
        assertEquals("content://image/123", vm.uiState.value.profileImageUri)
    }

    @Test
    fun `should toggle age dropdown expand and collapse`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isAgeDropdownExpanded)
        vm.onEvent(SignupUiEvent.OnAgeDropdownExpand)
        assertTrue(vm.uiState.value.isAgeDropdownExpanded)
        vm.onEvent(SignupUiEvent.OnAgeDropdownDismiss)
        assertFalse(vm.uiState.value.isAgeDropdownExpanded)
    }

    @Test
    fun `should auto-collapse dropdown after selecting age`() {
        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnAgeDropdownExpand)
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.FORTIES))
        assertEquals(AgeGroup.FORTIES, vm.uiState.value.ageGroup)
        assertFalse(vm.uiState.value.isAgeDropdownExpanded)
    }

    // ───── Submit ─────

    @Test
    fun `should emit NavigateToHome on successful signup`() = runTest {
        coEvery {
            signupUseCase(
                email = "a@b.c",
                password = "Aa1!Aa1!",
                nickname = "runner",
                profileImageUri = null,
                ageGroup = AgeGroup.THIRTIES,
                gender = Gender.MALE,
            )
        } returns AuthToken(accessToken = "a", refreshToken = "r")

        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnEmailChange("a@b.c"))
        vm.onEvent(SignupUiEvent.OnPasswordChange("Aa1!Aa1!"))
        vm.onEvent(SignupUiEvent.OnPasswordConfirmChange("Aa1!Aa1!"))
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.THIRTIES))
        vm.onEvent(SignupUiEvent.OnGenderSelect(Gender.MALE))

        vm.sideEffect.test {
            vm.onEvent(SignupUiEvent.OnSubmit)
            assertEquals(SignupSideEffect.NavigateToHome, awaitItem())
        }

        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should set errorMessage on signup failure`() = runTest {
        coEvery {
            signupUseCase(
                email = any(),
                password = any(),
                nickname = any(),
                profileImageUri = any(),
                ageGroup = any(),
                gender = any(),
            )
        } throws RuntimeException("backend not ready")

        val vm = viewModel()
        vm.onEvent(SignupUiEvent.OnNicknameChange("runner"))
        vm.onEvent(SignupUiEvent.OnAgeGroupSelect(AgeGroup.THIRTIES))
        vm.onEvent(SignupUiEvent.OnGenderSelect(Gender.MALE))
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

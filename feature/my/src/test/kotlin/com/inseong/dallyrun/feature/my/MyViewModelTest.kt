package com.inseong.dallyrun.feature.my

import app.cash.turbine.test
import com.inseong.dallyrun.core.domain.auth.DeleteMemberUseCase
import com.inseong.dallyrun.core.domain.auth.LogoutUseCase
import com.inseong.dallyrun.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val logoutUseCase = mockk<LogoutUseCase>(relaxUnitFun = true)
    private val deleteMemberUseCase = mockk<DeleteMemberUseCase>(relaxUnitFun = true)
    private fun viewModel() = MyViewModel(logoutUseCase, deleteMemberUseCase)

    // ───── 로그아웃 다이얼로그 ─────

    @Test
    fun `should show logout dialog on OnLogoutClick`() {
        val vm = viewModel()

        vm.onEvent(MyUiEvent.OnLogoutClick)

        assertTrue(vm.uiState.value.isLogoutDialogVisible)
    }

    @Test
    fun `should hide logout dialog on OnLogoutDismiss`() {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnLogoutClick)

        vm.onEvent(MyUiEvent.OnLogoutDismiss)

        assertFalse(vm.uiState.value.isLogoutDialogVisible)
    }

    @Test
    fun `should call logout and emit NavigateToLogin on OnLogoutConfirm`() = runTest {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnLogoutClick)

        vm.sideEffect.test {
            vm.onEvent(MyUiEvent.OnLogoutConfirm)
            assertEquals(MySideEffect.NavigateToLogin, awaitItem())
        }

        coVerify(exactly = 1) { logoutUseCase() }
        assertFalse(vm.uiState.value.isLogoutDialogVisible)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `should still emit NavigateToLogin when logout API fails`() = runTest {
        coEvery { logoutUseCase() } throws RuntimeException("network down")
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnLogoutClick)

        vm.sideEffect.test {
            vm.onEvent(MyUiEvent.OnLogoutConfirm)
            assertEquals(MySideEffect.NavigateToLogin, awaitItem())
        }
    }

    // ───── 회원탈퇴 다이얼로그 ─────

    @Test
    fun `should show delete dialog and reset password on OnDeleteAccountClick`() {
        val vm = viewModel()

        vm.onEvent(MyUiEvent.OnDeleteAccountClick)

        val state = vm.uiState.value
        assertTrue(state.isDeleteDialogVisible)
        assertEquals("", state.deletePasswordInput)
        assertNull(state.errorMessage)
    }

    @Test
    fun `should update password input and clear error on OnDeletePasswordChange`() {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)

        vm.onEvent(MyUiEvent.OnDeletePasswordChange("p@ss"))

        val state = vm.uiState.value
        assertEquals("p@ss", state.deletePasswordInput)
        assertNull(state.errorMessage)
    }

    @Test
    fun `should not call deleteMember when password is blank`() = runTest {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)

        vm.onEvent(MyUiEvent.OnDeleteConfirm)

        coVerify(exactly = 0) { deleteMemberUseCase(any()) }
    }

    @Test
    fun `should emit NavigateToLogin on successful deleteMember`() = runTest {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)
        vm.onEvent(MyUiEvent.OnDeletePasswordChange("p@ssw0rd"))

        vm.sideEffect.test {
            vm.onEvent(MyUiEvent.OnDeleteConfirm)
            assertEquals(MySideEffect.NavigateToLogin, awaitItem())
        }

        coVerify(exactly = 1) { deleteMemberUseCase("p@ssw0rd") }
        val state = vm.uiState.value
        assertFalse(state.isDeleteDialogVisible)
        assertFalse(state.isLoading)
        assertEquals("", state.deletePasswordInput)
    }

    @Test
    fun `should set errorMessage and keep dialog open on deleteMember failure`() = runTest {
        coEvery { deleteMemberUseCase(any()) } throws
            RuntimeException("비밀번호가 올바르지 않아요")

        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)
        vm.onEvent(MyUiEvent.OnDeletePasswordChange("wrong"))

        vm.onEvent(MyUiEvent.OnDeleteConfirm)

        val state = vm.uiState.value
        assertEquals("비밀번호가 올바르지 않아요", state.errorMessage)
        assertTrue(state.isDeleteDialogVisible)
        assertFalse(state.isLoading)
    }

    @Test
    fun `should hide delete dialog and clear input on OnDeleteDismiss`() {
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)
        vm.onEvent(MyUiEvent.OnDeletePasswordChange("typed"))

        vm.onEvent(MyUiEvent.OnDeleteDismiss)

        val state = vm.uiState.value
        assertFalse(state.isDeleteDialogVisible)
        assertEquals("", state.deletePasswordInput)
        assertNull(state.errorMessage)
    }

    @Test
    fun `should clear errorMessage on OnErrorDismiss`() = runTest {
        coEvery { deleteMemberUseCase(any()) } throws RuntimeException("boom")
        val vm = viewModel()
        vm.onEvent(MyUiEvent.OnDeleteAccountClick)
        vm.onEvent(MyUiEvent.OnDeletePasswordChange("x"))
        vm.onEvent(MyUiEvent.OnDeleteConfirm)

        vm.onEvent(MyUiEvent.OnErrorDismiss)

        assertNull(vm.uiState.value.errorMessage)
    }
}

package com.inseong.dallyrun.feature.login

import com.inseong.dallyrun.core.testing.MainDispatcherRule
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
}
